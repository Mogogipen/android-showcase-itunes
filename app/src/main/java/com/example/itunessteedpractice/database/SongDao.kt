package com.example.itunessteedpractice.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.itunessteedpractice.data.Song

@Dao
interface SongDao {

    @Query("SELECT * FROM song WHERE album_id = :albumId")
    suspend fun getSongs(albumId: Long): List<Song>

    @Insert
    suspend fun insertAll(songs: List<Song>)

}