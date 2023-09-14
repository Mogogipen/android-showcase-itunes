package com.example.itunessteedpractice.datasource

import android.graphics.Bitmap
import com.example.itunessteedpractice.data.Album
import com.example.itunessteedpractice.webresource.ImageWebResource
import com.example.itunessteedpractice.webresource.ItunesWebResource
import com.example.itunessteedpractice.webresource.response.AlbumResponseItem
import com.example.itunessteedpractice.webresource.response.AlbumSearchResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class AlbumDataSource {

    private val itunesWebResource = ItunesWebResource()
    private val imageWebResource = ImageWebResource()

    suspend fun searchByName(albumSearchText: String): List<Album> = withContext(IO) {
        searchForAlbumsWithAlbumArt(albumSearchText)
    }

    private suspend fun searchForAlbumsWithAlbumArt(albumSearchText: String): List<Album> {
        val searchResult = itunesWebResource.albumSearch(albumSearchText)
        return attachImagesToAlbums(searchResult.results)
    }

    private suspend fun attachImagesToAlbums(
        albumResults: List<AlbumResponseItem>
    ): List<Album> = coroutineScope {
        albumResults.map { async { it to imageWebResource.getImage(it.artworkUrl60) } }
                .awaitAll()
                .toMap()
                .flatMap { (albumResponseItem, image) -> listOf(Album(albumResponseItem, image)) }
    }

}