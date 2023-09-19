package com.example.itunessteedpractice.webresource

import com.example.itunessteedpractice.gson
import com.example.itunessteedpractice.okHttpClient
import com.example.itunessteedpractice.webresource.response.AlbumSearchResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.Request
import okhttp3.Response

class ItunesWebResource {

    suspend fun albumSearch(searchTerm: String): AlbumSearchResponse = withContext(IO) {
        requestAlbums(searchTerm)
    }

    private fun requestAlbums(searchTerm: String): AlbumSearchResponse {
        val response = okHttpClient.newCall(buildRequest(searchTerm))
                .execute()
        return response.processResponse()
    }

    private fun Response.processResponse(): AlbumSearchResponse {
        return body?.byteStream()?.reader()?.use { reader ->
            gson.fromJson(reader, AlbumSearchResponse::class.java)
        } ?: AlbumSearchResponse()
    }

    private fun buildRequest(searchTerm: String) = Request.Builder()
            .url(buildAlbumSearchUrl(searchTerm))
            .build()

    private fun buildAlbumSearchUrl(searchTerm: String) =
        "$BASE_SEARCH_URL?term=$searchTerm&entity=album&limit=10"

    companion object {
        private const val BASE_ITUNES_URL = """https://itunes.apple.com"""
        private const val BASE_SEARCH_URL = """$BASE_ITUNES_URL/search"""
    }

}