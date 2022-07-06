package com.example.test_paging.data.repo.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.test_paging.data.movie.db.MovieRemoteKeys

@Dao
interface RepoRemoteKeysDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: List<RepoRemoteKeys>)

    @Query("SELECT * FROM repo_remote_keys WHERE repoId = :repoId")
    suspend fun remoteKeysRepoId(repoId: Long): RepoRemoteKeys?

    @Query("DELETE FROM repo_remote_keys")
    suspend fun clear()
}