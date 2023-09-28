package com.example.itunessteedpractice.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.itunessteedpractice.data.Song
import com.example.itunessteedpractice.databinding.SongItemBinding

class SongAdapter(
    private val songs: List<Song>
): RecyclerView.Adapter<SongViewHolder>() {

    override fun getItemCount(): Int = songs.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = SongItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bindSong(songs[position])
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
        val seconds = (this % minutes) / 1000
        return "$minutes:$seconds"
    }

}
