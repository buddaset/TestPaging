package com.example.test_paging.data.movie.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.test_paging.data.repo.db.RepoRemoteKeys

@Dao
interface MovieRemoteKeysDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: List<MovieRemoteKeys>)

    @Query("SELECT * FROM movie_remote_keys WHERE movieId = :movieId")
    suspend fun remoteKeysMovieId(movieId: Long): MovieRemoteKeys?

    @Query("DELETE FROM movie_remote_keys")
    suspend fun clear()
}