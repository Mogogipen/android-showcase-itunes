package com.example.itunessteedpractice.model

import android.graphics.Bitmap
import com.example.itunessteedpractice.Bitmaps
import com.example.itunessteedpractice.data.Album
import com.example.itunessteedpractice.data.Song

sealed class AlbumDetailUiState {
    data class Success(
        val largeAlbumArt: Bitmap = Bitmaps.getBlankBitmap(),
        val albumTitle: String = "",
        val albumArtist: String = "",
        val albumReleaseYear: String = "",
        val songList: List<Song> = emptyList()
    ): AlbumDetailUiState() {
        constructor(largeAlbumArt: Bitmap, album: Album, songList: List<Song>): this(
            largeAlbumArt,
            album.title,
            album.releaseYear,
            album.artist,
            songList)
    }
    object Loading: AlbumDetailUiState()
    data class Error(val errorMessage: String): AlbumDetailUiState()
}
