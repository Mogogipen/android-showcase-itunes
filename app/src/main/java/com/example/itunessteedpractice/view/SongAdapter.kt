package com.example.itunessteedpractice.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.itunessteedpractice.data.Song
import com.example.itunessteedpractice.databinding.SongItemBinding

class SongAdapter: ListAdapter<Song, SongViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = SongItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bindSong(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Song>() {
            override fun areItemsTheSame(oldItem: Song, newItem: Song) = oldItem === newItem
            override fun areContentsTheSame(oldItem: Song, newItem: Song) = oldItem == newItem
        }
    }

}

class SongViewHolder(private val binding: SongItemBinding): ViewHolder(binding.root) {

    fun bindSong(song: Song) {
        binding.apply {
            songTitle.text = song.title
            songDuration.text = song.duration.toMinutesAndSeconds()
        }
    }

    private fun Long.toMinutesAndSeconds(): String {
        val minutes = this / (60 * 1000)
        val seconds = (this / 1000) % 60
        val secondsString = if (seconds < 10) "0$seconds" else seconds.toString()
        return "$minutes:$secondsString"
    }

}
