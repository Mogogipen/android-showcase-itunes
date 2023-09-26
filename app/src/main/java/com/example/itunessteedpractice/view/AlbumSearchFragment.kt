package com.example.itunessteedpractice.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.itunessteedpractice.data.Album
import com.example.itunessteedpractice.databinding.FragmentAlbumSearchBinding
import com.example.itunessteedpractice.model.AlbumSearchState
import com.example.itunessteedpractice.model.AlbumSearchUiState
import com.example.itunessteedpractice.viewmodel.AlbumSearchViewModel
import com.example.itunessteedpractice.viewmodel.SharedAlbumSearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AlbumSearchFragment: Fragment() {

    private var _binding: FragmentAlbumSearchBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedAlbumSearchViewModel by activityViewModels()
    private val viewModel: AlbumSearchViewModel by viewModels()

    private val albumAdapter get() = binding.albumList.adapter as AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumSearchBinding.inflate(inflater, container, false)
        binding.albumList.adapter = AlbumAdapter { album -> selectAlbum(album) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupListeners()
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { uiState ->
                    bindUiState(uiState)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindUiState(uiState: AlbumSearchUiState) {
        when(uiState.albumResults) {
            is AlbumSearchState.Error   -> displayError()
            AlbumSearchState.Loading    -> displayLoading()
            is AlbumSearchState.Success -> displayAlbumList(uiState.albumResults)
        }
    }

    private fun setupListeners() {
        with(binding) {
            albumSearchField.doOnTextChanged { text, _, _, _ ->
                text?.toString()?.let { viewModel.updateSearchText(it) }
            }
        }
    }

    private fun selectAlbum(album: Album) {
        sharedViewModel.selectAlbum(album)
        openDetails()
    }

    private fun openDetails() {
        //TODO
    }

    private fun displayAlbumList(albumResults: AlbumSearchState.Success) {
        hideLoading()
        hideError()
        albumAdapter.submitList(albumResults.albums)
    }

    private fun displayError() {
        hideLoading()
        albumAdapter.submitList(emptyList())
        showError()

    }

    private fun displayLoading() {
        showLoading()
    }

    private fun hideError() {
        binding.errorLabel.visibility = View.GONE
    }

    private fun showError() {
        binding.errorLabel.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loadingIndicator.visibility = View.GONE
    }

    private fun showLoading() {
        binding.loadingIndicator.visibility = View.VISIBLE
    }

}