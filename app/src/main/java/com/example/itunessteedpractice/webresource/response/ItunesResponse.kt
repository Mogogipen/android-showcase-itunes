package com.example.itunessteedpractice.webresource.response

data class ItunesResponse(
    val resultCount: Long = 0,
    val results: List<ItunesResponseItem> = emptyList()
)

data class ItunesResponseItem(
    val wrapperType: String = "",
    val collectionType: String = "",
    val artistId: Long = 0,
    val collectionId: Long = 0,
    val amgArtistId: Long = 0,
    val artistName: String = "",
    val collectionName: String = "",
    val collectionCensoredName: String = "",
    val artistViewUrl: String = "",
    val collectionViewUrl: String = "",
    val artworkUrl30: String = "",
    val artworkUrl60: String = "",
    val artworkUrl100: String = "",
    val collectionPrice: Double = 0.0,
    val collectionExplicitness: String = "",
    val trackCount: Long = 0,
    val copyright: String = "",
    val country: String = "",
    val currency: String = "",
    val releaseDate: String = "",
    val primaryGenreName: String = "",
    val kind: String = "",
    val trackId: Long = 0,
    val trackName: String = "",
    val trackCensoredName: String = "",
    val trackViewUrl: String = "",
    val previewUrl: String = "",
    val trackPrice: Double = 0.0,
    val trackExplicitness: String = "",
    val discCount: Long = 0,
    val discNumber: Long = 0,
    val trackNumber: Long = 0,
    val trackTimeMillis: Long = 0,
    val isStreamable: Boolean = false
)
