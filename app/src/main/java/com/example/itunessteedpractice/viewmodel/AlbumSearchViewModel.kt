package com.example.itunessteedpractice.viewmodel

import android.os.Parcelable
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itunessteedpractice.R
import com.example.itunessteedpractice.appContext
import com.example.itunessteedpractice.data.Album
import com.example.itunessteedpractice.model.AlbumSearchState
import com.example.itunessteedpractice.model.AlbumSearchUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize

class AlbumSearchViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {

    //TODO Remove once we start actually getting album art.
    private val genericImage get() = AppCompatResources.getDrawable(appContext, R.drawable.ic_launcher_foreground)?.toBitmap()

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

    private suspend fun search(searchText: String) = withContext(IO) {
        searchOperationFlow.emit(AlbumSearchState.Loading)
        try {
            //TODO: Search for albums
            delay(2000)
            if (System.currentTimeMillis() % 2 == 0L) {
                throw Exception(searchText)
            }
            val result = listOf(Album("Title", "artist", "2000", genericImage!!))
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
        private const val USER_INPUT = "userInput"
    }

}