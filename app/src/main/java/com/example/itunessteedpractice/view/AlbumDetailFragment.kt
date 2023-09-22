package com.example.itunessteedpractice.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.itunessteedpractice.databinding.FragmentAlbumDetailBinding
import com.example.itunessteedpractice.viewmodel.AlbumDetailViewModel
import com.example.itunessteedpractice.viewmodel.SharedAlbumSearchViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AlbumDetailFragment: Fragment() {

    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AlbumDetailViewModel by viewModels(
        { this },
        { AlbumDetailViewModel.buildExtras(activityViewModels<SharedAlbumSearchViewModel>().value) },
        { AlbumDetailViewModel.Factory })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}