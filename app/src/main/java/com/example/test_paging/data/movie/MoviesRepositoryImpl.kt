package com.example.test_paging.data

import androidx.paging.*
import com.example.test_paging.data.local.MovieDatabase
import com.example.test_paging.data.movie.Movie
import com.example.test_paging.data.movie.MovieRemoteMediator
import com.example.test_paging.data.movie.remote.MovieApi

import kotlinx.coroutines.flow.Flow



interface MoviesRepository {
    fun getPopularMovies(): Flow<PagingData<Movie>>
}

class MoviesRepositoryImpl(

    private val db: MovieDatabase,

    private val service: MovieApi
) : MoviesRepository {


    @OptIn(ExperimentalPagingApi::class)
    override fun getPopularMovies(): Flow<PagingData<Movie>> {
        val pagingSourceFactory = { db.movieDao().getPopularMovies() }

        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = MovieRemoteMediator(db, service),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }








    companion object {
        private const val PAGE_SIZE = 20
    }
}