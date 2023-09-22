package com.example.itunessteedpractice.model

import com.example.itunessteedpractice.data.Album

sealed class AlbumDetailUiState {
    data class Valid(val album: Album): AlbumDetailUiState()
    data class Error(val errorMessage: String): AlbumDetailUiState()
}
