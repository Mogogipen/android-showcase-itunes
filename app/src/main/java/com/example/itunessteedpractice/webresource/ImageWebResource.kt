package com.example.itunessteedpractice.webresource

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.itunessteedpractice.okHttpClient
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.Request

object ImageWebResource {

    suspend fun getImage(imageUrl: String): Bitmap = withContext(IO) {
        val response = okHttpClient.newCall(buildRequest(imageUrl))
                .execute()
        BitmapFactory.decodeStream(response.body?.byteStream())
    }

    private fun buildRequest(imageUrl: String) = Request.Builder()
            .url(imageUrl)
            .build()
}