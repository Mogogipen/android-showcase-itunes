package com.example.itunessteedpractice.datasource

import android.graphics.Bitmap
import com.example.itunessteedpractice.data.Album
import com.example.itunessteedpractice.webresource.ItunesWebResource
import com.example.itunessteedpractice.webresource.response.AlbumSearchResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class AlbumDataSource {

    private val itunesWebResource = ItunesWebResource()

    suspend fun searchByName(albumSearchText: String): List<Album> = withContext(IO) {
        itunesWebResource.albumSearch(albumSearchText).toAlbumList()
    }

    private fun AlbumSearchResponse.toAlbumList(): List<Album> {
        return results.map {
            Album(it.collectionName, it.artistName, it.releaseDate, Bitmap())
        }
    }

}