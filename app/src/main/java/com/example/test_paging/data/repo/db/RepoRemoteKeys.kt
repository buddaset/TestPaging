package com.example.test_paging.data.repo.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repo_remote_keys")
data class RepoRemoteKeys(
    @PrimaryKey
    val repoId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)