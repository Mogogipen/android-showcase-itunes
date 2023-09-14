package com.example.itunessteedpractice.webresource

import com.example.itunessteedpractice.gson
import com.example.itunessteedpractice.okHttpClient
import com.example.itunessteedpractice.webresource.response.AlbumSearchResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class ItunesWebResource {

    suspend fun albumSearch(searchTerm: String): AlbumSearchResponse = withContext(IO) {
        val response = okHttpClient.newCall(buildRequest(searchTerm))
                .execute()
        gson.fromJson(response.body.toString(), AlbumSearchResponse::class.java)
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