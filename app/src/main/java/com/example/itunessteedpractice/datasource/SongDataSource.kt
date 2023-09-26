package com.example.itunessteedpractice.datasource

import com.example.itunessteedpractice.appContext
import com.example.itunessteedpractice.data.Album
import com.example.itunessteedpractice.data.Song
import com.example.itunessteedpractice.database.AlbumSearchDatabase
import com.example.itunessteedpractice.webresource.ItunesWebResource

class SongDataSource {

    private val songDao = AlbumSearchDatabase.getInstance(appContext).songDao()

    fun selectAlbum(album: Album) {
        //TODO select album
    }

    suspend fun getSongsForAlbum(album: Album): List<Song> {
        val noSongsInDb = songDao.getSongs(album.id).lastOrNull() == null
        if (noSongsInDb) {
            val fetchResponse = ItunesWebResource.fetchSongsForAlbum(album)
            val songs = fetchResponse.results
                    .filter { it.wrapperType == SONG_TYPE }
                    .map { Song(it) }
            songDao.insertAll(songs)
        }
        return songDao.getSongs(album.id)
    }

    companion object {
        private const val SONG_TYPE = "track"
    }

}
