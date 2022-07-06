package com.example.test_paging.presentation.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.test_paging.data.movie.Movie
import com.example.test_paging.data.MoviesRepository
import kotlinx.coroutines.flow.Flow

class MovieViewModel(private val repository: MoviesRepository) : ViewModel() {

    val movies: Flow<PagingData<Movie>> = repository.getPopularMovies()
        .cachedIn(viewModelScope)

}