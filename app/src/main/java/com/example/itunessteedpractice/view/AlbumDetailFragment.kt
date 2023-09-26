package com.example.itunessteedpractice.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.itunessteedpractice.databinding.FragmentAlbumDetailBinding
import com.example.itunessteedpractice.model.AlbumDetailUiState
import com.example.itunessteedpractice.viewmodel.AlbumDetailViewModel
import com.example.itunessteedpractice.viewmodel.SharedAlbumSearchViewModel
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AlbumDetailFragment: Fragment() {

    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedAlbumSearchViewModel by activityViewModels()
    private val viewModel: AlbumDetailViewModel by viewModels()

    private val adapter = binding.songList.adapter as SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        binding.songList.adapter = SongAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    sharedViewModel.selectedAlbumState.collect { album ->
                        viewModel.selectAlbum(album ?: throw IllegalStateException())
                    }
                }
                launch {
                    viewModel.uiState.collect { uiState ->
                        bindUiState(uiState)
                    }
                }
            }
        }
    }

    private fun bindUiState(uiState: AlbumDetailUiState) {
        when (uiState) {
            is AlbumDetailUiState.Error   -> displayError(uiState.errorMessage)
            AlbumDetailUiState.Loading    -> displayLoading()
            is AlbumDetailUiState.Success -> displayAlbumDetail(uiState)
        }
    }

    private fun displayError(errorMessage: String) {
        hideLoading()
        hideContent()
        showError(errorMessage)
    }

    private fun displayLoading() {
        hideError()
        hideContent()
        showLoading()
    }

    private fun displayAlbumDetail(uiState: AlbumDetailUiState.Success) {
        hideError()
        hideLoading()
        showContent(uiState)
    }

    private fun showError(errorMessage: String) {
        binding.detailErrorLabel.apply {
            text = errorMessage
            visibility = View.VISIBLE
        }
    }

    private fun hideError() {
        binding.detailErrorLabel.visibility = View.GONE
    }

    private fun showLoading() {
        binding.loadingIndicator.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loadingIndicator.visibility = View.GONE
    }

    private fun hideContent() {
        binding.albumDetailContainer.visibility = View.GONE
    }

    private fun showContent(uiState: AlbumDetailUiState.Success) {
        binding.apply {
            albumDetailContainer.visibility = View.VISIBLE
            albumArt.setImageBitmap(uiState.largeAlbumArt)
            albumTitle.text = uiState.albumTitle
            albumArtist.text = uiState.albumArtist
            albumReleaseYear.text = uiState.albumReleaseYear
        }
        adapter.submitList(uiState.songList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}