package com.example.itunessteedpractice.viewmodel

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itunessteedpractice.R
import com.example.itunessteedpractice.appContext
import com.example.itunessteedpractice.model.AlbumSearchState
import com.example.itunessteedpractice.model.AlbumSearchUiState
import com.example.itunessteedpractice.operation.AlbumSearchOperation
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.parcelize.Parcelize

class AlbumSearchViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {

    private val albumSearchOperation = AlbumSearchOperation()

    private val searchErrorMessage get() = appContext.getString(R.string.search_error_message)

    private fun updateUserInput(input: UserInput) { savedStateHandle[USER_INPUT] = input }
    private val userInputFlow = savedStateHandle.getStateFlow(USER_INPUT, UserInput()).also { flow ->
        viewModelScope.launch {
            flow.collect { input ->
                if (input.searchText.isNotBlank()) albumSearchOperation.searchForAlbums(input.searchText)
            }
        }
    }
    private val searchOperationFlow = albumSearchOperation.state

    val uiState = combine(userInputFlow, searchOperationFlow) { userInput, searchState ->
        AlbumSearchUiState(
            userInput.searchText,
            searchState.asAlbumSearchState())
    }.stateIn(
        viewModelScope + IO,
        SharingStarted.WhileSubscribed(),
        AlbumSearchUiState()
    )

    fun updateSearchText(searchText: String) {
        updateUserInput(UserInput(searchText))

    }

    private fun AlbumSearchOperation.State.asAlbumSearchState(): AlbumSearchState {
        return when (this) {
            is AlbumSearchOperation.State.Completed -> AlbumSearchState.Success(albumList)
            AlbumSearchOperation.State.Failed       -> AlbumSearchState.Error(searchErrorMessage)
            AlbumSearchOperation.State.Running      -> AlbumSearchState.Loading
        }
    }

    @Parcelize
    private data class UserInput(val searchText: String = ""): Parcelable

    companion object {
        private const val USER_INPUT = "userInput"
    }

}