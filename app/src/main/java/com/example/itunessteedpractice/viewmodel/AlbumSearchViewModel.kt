package com.example.itunessteedpractice.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itunessteedpractice.R
import com.example.itunessteedpractice.appContext
import com.example.itunessteedpractice.data.Album
import com.example.itunessteedpractice.generics.ProcessState
import com.example.itunessteedpractice.model.AlbumSearchState
import com.example.itunessteedpractice.model.AlbumSearchUiState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class AlbumSearchViewModel(val savedStateHandle: SavedStateHandle): ViewModel() {

    private val searchErrorMessage get() = appContext.getString(R.string.search_error_message)

    private val userInputFlow = savedStateHandle.getStateFlow(USER_INPUT, UserInput())
    private fun updateUserInput(input: UserInput) { savedStateHandle[USER_INPUT] = input }
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
        search(searchText)
    }

    private fun search(searchText: String) = viewModelScope.launch(IO) {
        searchOperationFlow.emit(AlbumSearchState.Loading)
        try {
            //TODO: Search for albums
            delay(2000)
            if (System.currentTimeMillis() % 2 == 0L) {
                throw Exception(searchText)
            }
            searchOperationFlow.emit(AlbumSearchState.Success(listOf(Album("Title", "artist", "2000"))))
        } catch (e: Exception) {
            searchOperationFlow.emit(AlbumSearchState.Error(searchErrorMessage))
        }
    }

    private data class UserInput(val searchText: String = "")

    companion object {
        private const val USER_INPUT = "userInput"
    }

}