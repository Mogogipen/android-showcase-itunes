package com.example.itunessteedpractice.viewmodel

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itunessteedpractice.R
import com.example.itunessteedpractice.appContext
import com.example.itunessteedpractice.data.Album
import com.example.itunessteedpractice.datasource.AlbumDataSource
import com.example.itunessteedpractice.model.AlbumSearchState
import com.example.itunessteedpractice.model.AlbumSearchUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize

class AlbumSearchViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val sharedViewModel: SharedAlbumSearchViewModel
): ViewModel() {

    private val albumDataSource = AlbumDataSource()

    private val searchErrorMessage get() = appContext.getString(R.string.search_error_message)

    private fun updateUserInput(input: UserInput) { savedStateHandle[USER_INPUT] = input }
    private val userInputFlow = savedStateHandle.getStateFlow(USER_INPUT, UserInput()).also { flow ->
        viewModelScope.launch {
            flow.collectLatest { input ->
                if (input.searchText.isNotBlank()) search(input.searchText)
            }
        }
    }
    private val searchOperationFlow = MutableStateFlow<AlbumSearchState>(AlbumSearchState.Success())

    val uiState = combine(userInputFlow, searchOperationFlow) { userInput, searchState ->
        AlbumSearchUiState(userInput.searchText, searchState)
    }.stateIn(
        viewModelScope + IO,
        SharingStarted.WhileSubscribed(),
        AlbumSearchUiState()
    )

    fun updateSearchText(searchText: String) {
        updateUserInput(UserInput(searchText)) //If there's ever any more user input, we would use copy()
    }

    fun selectAlbum(album: Album) {
        sharedViewModel.selectedAlbum = album
    }

    private suspend fun search(searchText: String) = withContext(IO) {
        searchOperationFlow.emit(AlbumSearchState.Loading)
        try {
            val result = albumDataSource.searchByName(searchText)
            searchOperationFlow.emit(AlbumSearchState.Success(result))
        } catch (_: CancellationException) {
            searchOperationFlow.emit(AlbumSearchState.Success())
        } catch (e: Exception) {
            Log.e(AlbumSearchViewModel::class.simpleName, "An error!", e)
            searchOperationFlow.emit(AlbumSearchState.Error(searchErrorMessage))
        }
    }

    @Parcelize
    private data class UserInput(val searchText: String = ""): Parcelable

    companion object {
        //TODO create factory to inject the shared view model
        private const val USER_INPUT = "userInput"
    }

}