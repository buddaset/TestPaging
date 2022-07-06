package com.example.test_paging.data.movie.remote

import com.example.test_paging.data.movie.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesResponse(

    @SerialName("results")
    val results: List<Movie>,

    )