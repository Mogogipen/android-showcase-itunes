package com.example.itunessteedpractice

import com.google.gson.Gson
import okhttp3.OkHttpClient

val okHttpClient = OkHttpClient()
val gson = Gson()
val appContext get() = ItunesApplication.appContext