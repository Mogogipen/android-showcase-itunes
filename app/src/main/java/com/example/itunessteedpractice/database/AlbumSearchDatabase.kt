package com.example.itunessteedpractice.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.itunessteedpractice.data.Song

@Database(entities = [Song::class], version = 1)
abstract class AlbumSearchDatabase: RoomDatabase() {

    abstract fun songDao(): SongDao

    companion object {
        private const val DATABASE_NAME = "album_search_database"
        @Volatile private var instance: AlbumSearchDatabase? = null

        fun getInstance(context: Context): AlbumSearchDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context): AlbumSearchDatabase =
            Room.databaseBuilder(context, AlbumSearchDatabase::class.java, DATABASE_NAME)
                    .build()
    }

}