package com.example.itunessteedpractice.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.itunessteedpractice.data.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

    @Query("SELECT * FROM song WHERE album_id = :albumId")
    fun getSongs(albumId: Long): Flow<List<Song>>

    @Insert
    fun insertAll(songs: List<Song>)

}