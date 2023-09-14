package com.example.itunessteedpractice.data

import android.graphics.Bitmap
import com.example.itunessteedpractice.helpers.dates.Dates
import com.example.itunessteedpractice.webresource.response.AlbumResponseItem

data class Album(
    val title: String,
    val artist: String,
    val releaseYear: String,
    val albumArt: Bitmap
) {
    constructor(albumResponseItem: AlbumResponseItem, albumArt: Bitmap): this(
        albumResponseItem.collectionName,
        albumResponseItem.artistName,
        Dates.yearFromTimeString(albumResponseItem.releaseDate),
        albumArt)
}
