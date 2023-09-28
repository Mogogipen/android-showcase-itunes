package com.example.itunessteedpractice.model

import com.example.itunessteedpractice.data.Album

data class AlbumSearchUiState(
    val searchText: String = "",
    val albumResults: AlbumSearchState = AlbumSearchState.Success())

sealed class AlbumSearchState {
    data class Success(val albums: List<Album> = emptyList()): AlbumSearchState()
    object Loading: AlbumSearchState()
    data class Error(val errorMessage: String): AlbumSearchState()
}