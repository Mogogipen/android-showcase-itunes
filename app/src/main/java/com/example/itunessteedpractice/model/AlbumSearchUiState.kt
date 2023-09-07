package com.example.itunessteedpractice.model

import androidx.annotation.StringRes
import com.example.itunessteedpractice.data.Album

data class AlbumSearchUiState(
    val searchText: String,
    val albumResults: AlbumSearchState)

sealed class AlbumSearchState {
    data class Success(val albums: List<Album> = emptyList()): AlbumSearchState()
    object Loading: AlbumSearchState()
    data class Error(val errorMessage: String): AlbumSearchState() //TODO: Should we simply show a message? Or is there a better approach?
}