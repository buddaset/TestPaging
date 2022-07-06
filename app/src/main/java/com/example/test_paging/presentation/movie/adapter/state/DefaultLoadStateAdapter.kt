package com.example.test_paging.presentation.movie.adapter.state

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter


class DefaultLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState
    ): LoadStateViewHolder {
        return LoadStateViewHolder.create(parent, retry)
    }
}



