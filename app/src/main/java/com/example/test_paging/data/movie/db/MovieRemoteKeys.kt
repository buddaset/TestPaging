package com.example.test_paging.data.movie.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "movie_remote_keys")
data class MovieRemoteKeys(
    @PrimaryKey
    val movieId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)
