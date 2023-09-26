package com.example.itunessteedpractice.data

import android.graphics.Bitmap
import com.example.itunessteedpractice.helpers.dates.Dates
import com.example.itunessteedpractice.webresource.response.ItunesResponseItem

data class Album(
    val title: String,
    val artist: String,
    val releaseYear: String,
    val albumArt: Bitmap,
    val largeImageUrl: String,
    val id: Long
) {
    constructor(albumResponseItem: ItunesResponseItem, albumArt: Bitmap): this(
        albumResponseItem.collectionName,
        albumResponseItem.artistName,
        Dates.yearFromTimeString(albumResponseItem.releaseDate),
        albumArt,
        albumResponseItem.artworkUrl100,
        albumResponseItem.collectionId
    )
}
