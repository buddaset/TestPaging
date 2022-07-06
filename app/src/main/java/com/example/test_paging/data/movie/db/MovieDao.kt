package com.example.test_paging.data.movie.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.test_paging.data.movie.Movie


@Dao
interface MovieDao {



    @Query("SELECT * FROM movies")
    fun getPopularMovies(): PagingSource<Int, Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovie(list: List<Movie>)

    @Query("DELETE FROM movies")
    suspend fun clearAllMovie()

    @Query("SELECT COUNT('id') FROM movies")
    suspend fun getCountMovies() : Int
}