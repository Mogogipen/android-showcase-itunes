package com.example.itunessteedpractice.datasource

import android.graphics.Bitmap
import com.example.itunessteedpractice.data.Album
import com.example.itunessteedpractice.webresource.ImageWebResource
import com.example.itunessteedpractice.webresource.ItunesWebResource
import com.example.itunessteedpractice.webresource.response.ItunesResponseItem
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class AlbumDataSource {

    suspend fun searchByName(albumSearchText: String): List<Album> = withContext(IO) {
        searchForAlbumsWithAlbumArt(albumSearchText)
    }

    private suspend fun searchForAlbumsWithAlbumArt(albumSearchText: String): List<Album> {
        val searchResult = ItunesWebResource.albumSearch(albumSearchText)
        return attachImagesToAlbums(searchResult.results)
    }

    private suspend fun attachImagesToAlbums(
        albumResults: List<ItunesResponseItem>
    ): List<Album> = coroutineScope {
        albumResults.filter { it.wrapperType == ALBUM_TYPE }
                .map { async { it to it.fetchImage() } }
                .awaitAll()
                .toMap()
                .flatMap { (albumResponseItem, image) -> listOf(Album(albumResponseItem, image)) }
    }

    private suspend fun ItunesResponseItem.fetchImage(): Bitmap {
        return ImageWebResource.getImage(artworkUrl60)
    }

    companion object {
        private const val ALBUM_TYPE = "collection"
    }

}