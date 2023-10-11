package com.movie.trvn.core.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.movie.trvn.core.domain.model.Movie
import com.movie.trvn.databinding.MovieItemBinding


class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private val listMovies = ArrayList<Movie>()
    @SuppressLint("NotifyDataSetChanged")
    fun setListMovies(listMovies: List<Movie>) {
        val diffCallback = NoteDiffCallback(this.listMovies, listMovies)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listMovies.clear()
        this.listMovies.addAll(listMovies)
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(listMovies[position])
    }
    override fun getItemCount(): Int {
        return listMovies.size
    }
    inner class MovieViewHolder(private val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            with(binding) {
                tvItemTitle.text = movie.title
                tvItemReleaseDate.text = movie.releaseDate
            }
        }
    }
}

class NoteDiffCallback(private val mOldMovieList: List<Movie>, private val mNewMovieList: List<Movie>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldMovieList.size
    }
    override fun getNewListSize(): Int {
        return mNewMovieList.size
    }
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldMovieList[oldItemPosition].id == mNewMovieList[newItemPosition].id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMovie = mOldMovieList[oldItemPosition]
        val newMovie = mNewMovieList[newItemPosition]
        return oldMovie.title == newMovie.title && oldMovie.releaseDate == newMovie.releaseDate
    }
}