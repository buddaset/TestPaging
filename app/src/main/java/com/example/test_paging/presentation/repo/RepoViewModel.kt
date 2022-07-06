

package com.example.test_paging.presentation.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.android.codelabs.paging.data.RepoRepository
import com.example.android.codelabs.paging.model.Repo
import kotlinx.coroutines.flow.*

/**
 * ViewModel for the [SearchRepositoriesActivity] screen.
 * The ViewModel works with the [RepoRepository] to get the data.
 */
class RepoViewModel(
    private val repository: RepoRepository,
) : ViewModel() {

    private val query = "Android"


    val repos: Flow<PagingData<Repo>> = repository.getSearchResultStream(query)
        .cachedIn(viewModelScope)
}