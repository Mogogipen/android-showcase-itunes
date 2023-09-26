package com.example.itunessteedpractice.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.itunessteedpractice.data.Album
import kotlinx.coroutines.flow.MutableStateFlow

class SharedAlbumSearchViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {

    // val selectedAlbumState = savedStateHandle.getStateFlow<Long?>(SELECTED_ALBUM_KEY, null)
    // private fun updateSelectedAlbum(albumId: Long) { savedStateHandle[SELECTED_ALBUM_KEY] = albumId }

    val selectedAlbumState = MutableStateFlow<Album?>(null)

    fun selectAlbum(album: Album) {
        selectedAlbumState.value = album
    }

    companion object {
        const val SELECTED_ALBUM_KEY = "selectedAlbum"
    }

}