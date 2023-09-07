package com.example.itunessteedpractice

import android.app.Application
import android.content.Context

class ItunesApplication: Application() {

    companion object {
        lateinit var appContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext
    }
}