/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.test_paging

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.codelabs.paging.api.GithubService
import com.example.android.codelabs.paging.data.RepoRepository
import com.example.test_paging.data.MoviesRepository
import com.example.test_paging.data.MoviesRepositoryImpl
import com.example.test_paging.data.local.MovieDatabase
import com.example.test_paging.data.movie.remote.MovieApi
import com.example.test_paging.presentation.movie.MovieViewModel
import com.example.test_paging.presentation.repo.RepoViewModel

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    /**
     * Creates an instance of [GithubRepository] based on the [GithubService] and a
     * [GithubLocalCache]
     */
    private fun provideMovieRepository(context: Context): MoviesRepository {
        return MoviesRepositoryImpl(MovieDatabase.getInstance(context), MovieApi.create())
    }

    private fun provideRepoRepository(context: Context): RepoRepository =
        RepoRepository(GithubService.create(), MovieDatabase.getInstance(context))

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideMovieRepository(context),
        provideRepoRepository(context))
    }
}

class ViewModelFactory(private val movieRepository: MoviesRepository,
private val repoRepository: RepoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when(modelClass) {
            MovieViewModel::class.java -> MovieViewModel(movieRepository)
            RepoViewModel::class.java -> RepoViewModel(repoRepository)
            else -> throw IllegalStateException("Unknown viewModel")
        } as T

}
