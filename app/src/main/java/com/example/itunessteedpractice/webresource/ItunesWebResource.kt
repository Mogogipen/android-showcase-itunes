package com.example.itunessteedpractice.webresource

import com.example.itunessteedpractice.data.Album
import com.example.itunessteedpractice.gson
import com.example.itunessteedpractice.okHttpClient
import com.example.itunessteedpractice.webresource.response.ItunesResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.Request
import okhttp3.Response

object ItunesWebResource {

    private const val BASE_ITUNES_URL = """https://itunes.apple.com"""
    private const val BASE_SEARCH_URL = """$BASE_ITUNES_URL/search"""
    private const val BASE_LOOKUP_URL = """$BASE_ITUNES_URL/lookup"""

    suspend fun albumSearch(searchTerm: String): ItunesResponse = withContext(IO) {
        requestAlbums(searchTerm)
    }

    suspend fun fetchSongsForAlbum(album: Album): ItunesResponse = withContext(IO) {
        fetchSongs(album)
    }

    //region Album search
    private fun requestAlbums(searchTerm: String): ItunesResponse {
        val response = okHttpClient.newCall(buildAlbumSearchRequest(searchTerm))
                .execute()
        return response.processItunesResponse()
    }

    private fun Response.processItunesResponse(): ItunesResponse {
        return body?.byteStream()?.reader()?.use { reader ->
            gson.fromJson(reader, ItunesResponse::class.java)
        } ?: ItunesResponse()
    }

    private fun buildAlbumSearchRequest(searchTerm: String) = Request.Builder()
            .url(buildAlbumSearchUrl(searchTerm))
            .build()

    private fun buildAlbumSearchUrl(searchTerm: String) =
        "$BASE_SEARCH_URL?term=$searchTerm&entity=album&limit=10"
    //endregion

    //region Song fetch
    private fun fetchSongs(album: Album): ItunesResponse {
        val response = okHttpClient.newCall(buildSongFetchRequest(album))
                .execute()
        return response.processSongFetchResponse()
    }

    private fun Response.processSongFetchResponse(): ItunesResponse {
        return body?.byteStream()?.reader()?.use { reader ->
            gson.fromJson(reader, ItunesResponse::class.java)
        } ?: ItunesResponse()
    }

    private fun buildSongFetchRequest(album: Album) = Request.Builder()
            .url(buildSongFetchUrl(album))
            .build()

    private fun buildSongFetchUrl(album: Album) =
        "$BASE_LOOKUP_URL?id=${album.id}&entity=song"
    //endregion

}