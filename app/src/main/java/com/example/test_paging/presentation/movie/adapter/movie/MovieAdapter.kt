package com.example.test_paging.presentation.movie.adapter.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.test_paging.data.movie.Movie
import com.example.test_paging.databinding.ViewHolderMovieBinding


class MovieAdapter() : PagingDataAdapter<Movie, MovieViewHolder>(MovieCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(
            ViewHolderMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position) ?: return
        holder.bind(movie)
    }
}


private class MovieCallback: DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldData: Movie, newData: Movie): Boolean =
        oldData.remoteId == newData.remoteId // TODO check update recycler

    override fun areContentsTheSame(oldData: Movie, newData: Movie): Boolean =
        oldData == newData

}

