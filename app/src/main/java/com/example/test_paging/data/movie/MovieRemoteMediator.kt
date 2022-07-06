package com.example.test_paging.data.movie

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.test_paging.data.repo.db.RepoRemoteKeys

import com.example.test_paging.data.local.MovieDatabase
import com.example.test_paging.data.movie.db.MovieRemoteKeys
import com.example.test_paging.data.movie.remote.MovieApi


@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val db: MovieDatabase,
    private val service: MovieApi
) : RemoteMediator<Int, Movie>() {

    private val movieDao = db.movieDao()
    private val remoteKeysDao = db.movieRemoteKeysDao()


    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {

        val page: Int = when (loadType) {
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                Log.d(
                    "Mediator",
                    "APPEND next page --- ${remoteKeys?.nextKey}  prev page --- ${remoteKeys?.prevKey}"
                )
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = (remoteKeys != null))

                nextKey
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyFirstItem(state)
                Log.d(
                    "Mediator",
                    "PREPEND next page --- ${remoteKeys?.nextKey}  prev page --- ${remoteKeys?.prevKey}"
                )
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = (remoteKeys != null))

                prevKey
            }

            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                Log.d(
                    "Mediator",
                    "REFRESH next page --- ${remoteKeys?.nextKey}  prev page --- ${remoteKeys?.prevKey}"
                )
                remoteKeys?.nextKey?.minus(1) ?: START_PAGE
            }
        }

        return try {

            Log.d("Mediator", " page ---- $page.  loadType ---- $loadType")
            val movies = service.loadMoviesPopular(page, state.config.pageSize).results
            val endOfPaginationReached = movies.isEmpty()
            Log.d(
                "Mediator",
                "movies size - ${movies.size}       endOfReached -- $endOfPaginationReached"
            )

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clear()
                    movieDao.clearAllMovie()
                }
                val prevKey = if (page == START_PAGE) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = movies.map {
                    MovieRemoteKeys(movieId = it.remoteId, prevKey = prevKey, nextKey = nextKey)
                }
                Log.d("Mediator", "keys  $keys")
                remoteKeysDao.insert(keys)
                movieDao.insertAllMovie(movies)
            }


            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            Log.d("Mediator", "mediator ---- exception block   ---- $e")
            return MediatorResult.Error(e)
        }
    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): MovieRemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                Log.d("Mediator", " movieId ---- ${movie.idDB} ---- APPEND ")
                remoteKeysDao.remoteKeysMovieId(movie.remoteId)
            }
    }


    private suspend fun getRemoteKeyFirstItem(state: PagingState<Int, Movie>): MovieRemoteKeys? {
        return state.pages.firstOrNull() { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie ->
                Log.d("Mediator", " movieId ---- ${movie.idDB} ---- PREPEND ")
                remoteKeysDao.remoteKeysMovieId(movie.remoteId)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Movie>): MovieRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.remoteId?.let { remoteMovieId ->
                Log.d("Mediator", " movieId ---- ${remoteMovieId}  ---- REFRESH ")
                remoteKeysDao.remoteKeysMovieId(remoteMovieId)
            }
        }
    }


    companion object {
        private const val START_PAGE = 1

        private const val POPULAR_MOVIES = "popular_movies"
    }
}