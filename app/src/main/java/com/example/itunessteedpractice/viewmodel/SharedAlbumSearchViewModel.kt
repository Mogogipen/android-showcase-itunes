package com.example.itunessteedpractice.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.itunessteedpractice.data.Album

class SharedAlbumSearchViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {

    val selectedAlbumState = savedStateHandle.getStateFlow<Long?>(SELECTED_ALBUM_KEY, null)
    private fun updateSelectedAlbum(albumId: Long) { savedStateHandle[SELECTED_ALBUM_KEY] = albumId }

    fun selectAlbum(album: Album) {
        updateSelectedAlbum(album.id)
    }

    companion object {
        const val SELECTED_ALBUM_KEY = "selectedAlbum"
    }

}