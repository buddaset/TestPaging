package com.example.android.codelabs.paging.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.android.codelabs.paging.api.GithubService
import com.example.android.codelabs.paging.api.IN_QUALIFIER
import com.example.test_paging.data.repo.db.RepoRemoteKeys
import com.example.android.codelabs.paging.model.Repo
import com.example.test_paging.data.local.MovieDatabase
import okio.IOException
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class RepoRemoteMediator(
    private val query: String,
    private val service: GithubService,
    private val db: MovieDatabase
): RemoteMediator<Int, Repo>() {

    private val repoDao = db.repoDao()
    private val remoteKeysDao = db.repoRemoteKeysDao()





    override suspend fun load(loadType: LoadType, state: PagingState<Int, Repo>): MediatorResult {

        val page: Int = when (loadType) {
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return  MediatorResult.Success(endOfPaginationReached = (remoteKeys != null))
                nextKey
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = (remoteKeys != null))
                prevKey
            }

            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: GITHUB_STARTING_PAGE_INDEX
            }
        }

        Log.d("Mediator", " page ---- $page.  loadType ---- $loadType" )
        val apiQuery = query + IN_QUALIFIER

        try {
            val apiResponse = service.searchRepos(apiQuery, page, state.config.pageSize)

            val repos = apiResponse.items
            val endOfPaginationReached = repos.isEmpty()
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clear()
                    repoDao.clearRepos()
                }
                val prevKey = if (page == GITHUB_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = repos.map {
                    RepoRemoteKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                remoteKeysDao.insert(keys)
                repoDao.insertAll(repos)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException){
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int,Repo>) : RepoRemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                Log.d("Mediator", " repoId ---- ${repo.id} ---- APPEND  " )
                remoteKeysDao.remoteKeysRepoId(repo.id)
            }
    }


    private suspend fun getRemoteKeyFirstItem(state: PagingState<Int,Repo>) : RepoRemoteKeys? {
        return state.pages.firstOrNull() {it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                Log.d("Mediator", " repoId ---- ${repo.id} ---- PREPEND  " )
               remoteKeysDao.remoteKeysRepoId(repo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Repo>) : RepoRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                Log.d("Mediator", " repoId ---- ${repoId} ---- REFRESH " )
                remoteKeysDao.remoteKeysRepoId(repoId)
            }
        }
    }

    companion object {
        private const val GITHUB_STARTING_PAGE_INDEX = 1
    }
}