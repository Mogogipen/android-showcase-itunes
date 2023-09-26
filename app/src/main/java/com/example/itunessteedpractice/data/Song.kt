package com.example.itunessteedpractice.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.itunessteedpractice.webresource.response.ItunesResponseItem

@Entity(tableName = Song.TABLE_NAME)
data class Song(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_NAME_ID)
    val id: Long = 0L,
    @ColumnInfo(name = COLUMN_NAME_TITLE)
    val title: String = "",
    @ColumnInfo(name = COLUMN_NAME_DURATION)
    val duration: Long = 0L,
    @ColumnInfo(name = COLUMN_NAME_ALBUM_ID)
    val albumId: Long = 0L
) {

    constructor(itunesResponseItem: ItunesResponseItem): this(
        itunesResponseItem.trackId,
        itunesResponseItem.trackName,
        itunesResponseItem.trackTimeMillis,
        itunesResponseItem.collectionId)

    companion object {
        const val TABLE_NAME = "song"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DURATION = "duration"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_ALBUM_ID = "album_id"
    }
}
