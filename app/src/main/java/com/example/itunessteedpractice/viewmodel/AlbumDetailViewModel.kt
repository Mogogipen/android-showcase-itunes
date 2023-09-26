package com.example.itunessteedpractice.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itunessteedpractice.R
import com.example.itunessteedpractice.appContext
import com.example.itunessteedpractice.data.Album
import com.example.itunessteedpractice.datasource.SongDataSource
import com.example.itunessteedpractice.model.AlbumDetailUiState
import com.example.itunessteedpractice.webresource.ImageWebResource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlbumDetailViewModel: ViewModel() {

    private val songDataSource = SongDataSource()

    private val detailErrorMessage get() = appContext.getString(R.string.album_detail_error)

    private val selectedAlbumFlow = MutableStateFlow<Album?>(null).also { flow ->
        viewModelScope.launch {
            flow.collectLatest { album ->
                album?.let { requiredAlbum ->
                    fetchSongs(requiredAlbum)
                }
            }
        }
    }
    private val mutableUiState = MutableStateFlow<AlbumDetailUiState>(AlbumDetailUiState.Success())
    val uiState = mutableUiState.asStateFlow()

    fun selectAlbum(album: Album) {
        selectedAlbumFlow.value = album
    }

    private suspend fun fetchSongs(album: Album) {
        mutableUiState.emit(AlbumDetailUiState.Loading)
        try {
            coroutineScope {
                val deferredSongs = async { songDataSource.getSongsForAlbum(album) }
                val deferredImage = async { ImageWebResource.getImage(album.largeImageUrl) }
                mutableUiState.emit(
                    AlbumDetailUiState.Success(deferredImage.await(), album, deferredSongs.await()))
            }
        } catch (_: CancellationException) {
            mutableUiState.emit(AlbumDetailUiState.Success())
        } catch (e: Exception) {
            Log.e(AlbumDetailViewModel::class.simpleName, "Error occurred fetching songs!!")
            mutableUiState.emit(AlbumDetailUiState.Error(detailErrorMessage))
        }
    }


}
