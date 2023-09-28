package com.example.itunessteedpractice.operation

import android.util.Log
import com.example.itunessteedpractice.data.Album
import com.example.itunessteedpractice.datasource.AlbumDataSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AlbumSearchOperation {

    private val albumDataSource = AlbumDataSource()

    val state = MutableStateFlow<State>(State.Completed())

    private var job: Job? = null

    suspend fun searchForAlbums(searchTerm: String) {
        cancelRunningJob()
        coroutineScope {
            job = launch(IO) { runOperation(searchTerm) }
        }
    }

    private fun cancelRunningJob() {
        synchronized(this) {
            job?.let { safeJob ->
                if (safeJob.isActive) {
                    safeJob.cancel(null)
                }
            }
        }
    }

    private suspend fun runOperation(searchTerm: String) {
        state.emit(State.Running)
        try {
            val result = albumDataSource.searchByName(searchTerm)
            state.emit(State.Completed(result))
        } catch (_: CancellationException) { // ignore and allow cancellation
        } catch (e: Exception) {
            Log.e(AlbumSearchOperation::class.simpleName, "An error!", e)
            state.emit(State.Failed)
        }
    }

    sealed class State {
        object Running: State()
        data class Completed(val albumList: List<Album> = emptyList()): State()
        object Failed: State()
    }

}