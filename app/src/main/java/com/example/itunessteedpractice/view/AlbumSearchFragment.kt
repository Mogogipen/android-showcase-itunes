package com.example.itunessteedpractice.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.itunessteedpractice.databinding.FragmentAlbumSearchBinding
import com.example.itunessteedpractice.model.AlbumSearchState
import com.example.itunessteedpractice.model.AlbumSearchUiState
import com.example.itunessteedpractice.viewmodel.AlbumSearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AlbumSearchFragment: Fragment() {

    private var _binding: FragmentAlbumSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AlbumSearchViewModel by viewModels()

    private val albumAdapter get() = binding.albumList.adapter as AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumSearchBinding.inflate(inflater, container, false)
        binding.albumList.adapter = AlbumAdapter()
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

    private fun displayAlbumList(albumResults: AlbumSearchState.Success) {
        hideLoading()
        albumAdapter.submitList(albumResults.albums)
    }

    private fun displayError() {
        hideLoading()
        AlertDialog.Builder(requireContext())
                .setTitle("Error")
                .setMessage("Could not get result")
                .setNeutralButton("OK", null)
                .show()
    }

    private fun hideLoading() {
        binding.loadingIndicator.visibility = View.GONE
    }

    private fun displayLoading() {
        binding.loadingIndicator.visibility = View.VISIBLE
    }

}