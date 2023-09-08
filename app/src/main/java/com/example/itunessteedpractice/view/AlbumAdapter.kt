package com.example.itunessteedpractice.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.itunessteedpractice.data.Album
import com.example.itunessteedpractice.databinding.AlbumItemBinding

class AlbumAdapter: ListAdapter<Album, AlbumViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AlbumItemBinding.inflate(inflater, parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bindAlbum(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Album>() {
            override fun areItemsTheSame(oldItem: Album, newItem: Album) = oldItem === newItem
            override fun areContentsTheSame(oldItem: Album, newItem: Album) = oldItem == newItem
        }
    }
}

class AlbumViewHolder(private val binding: AlbumItemBinding): ViewHolder(binding.root) {

    fun bindAlbum(album: Album) = binding.apply {
        albumTitle.text = album.title
        artist.text = album.artist
        releaseYear.text = album.releaseYear
        //TODO: Bind album image
    }

}
