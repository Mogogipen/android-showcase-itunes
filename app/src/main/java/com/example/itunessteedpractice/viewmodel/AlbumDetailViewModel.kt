package com.example.itunessteedpractice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.itunessteedpractice.generics.CreationKey

class AlbumDetailViewModel(
    private val sharedViewModel: SharedAlbumSearchViewModel
): ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AlbumDetailViewModel(
                    this[SHARED_VIEW_MODEL_KEY] ?: throw InitializationException()
                )
            }
        }
        fun buildExtras(sharedViewModel: SharedAlbumSearchViewModel): CreationExtras =
            MutableCreationExtras().apply {
                this[SHARED_VIEW_MODEL_KEY] = sharedViewModel
            }
        private val SHARED_VIEW_MODEL_KEY = CreationKey<SharedAlbumSearchViewModel>()
    }

    class InitializationException: Exception(MESSAGE) {
        companion object{
            const val MESSAGE = "Did you use buildExtras() to create the view model?"
        }
    }

}
