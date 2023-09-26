package com.example.itunessteedpractice.model

import com.example.itunessteedpractice.data.AlbumDetail

sealed class AlbumDetailUiState {
    data class Success(val album: AlbumDetail): AlbumDetailUiState()
    object Loading: AlbumDetailUiState()
    data class Error(val errorMessage: String): AlbumDetailUiState()
}
