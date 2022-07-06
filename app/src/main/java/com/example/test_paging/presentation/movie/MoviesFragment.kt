package com.example.test_paging.presentation.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.test_paging.Injection

import com.example.test_paging.presentation.movie.adapter.movie.MovieAdapter
import com.example.test_paging.presentation.movie.adapter.state.DefaultLoadStateAdapter
import com.example.test_paging.databinding.FragmentMoviesBinding
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class MoviesFragment : Fragment() {


    private lateinit var binding: FragmentMoviesBinding

    private val vm: MovieViewModel by viewModels {
        Injection.provideViewModelFactory(requireActivity().applicationContext)
    }

    private val mAdapter: MovieAdapter by lazy {  MovieAdapter()  }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeMovies()
        initAdapter()
        initListeners()


    }

    @OptIn(FlowPreview::class)
    private fun initListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            mAdapter.refresh()
        }
        mAdapter.loadStateFlow
            .debounce(300)
            .onEach { refreshLoadState(it.refresh)}
            .launchIn(lifecycleScope)
    }

    private fun refreshLoadState(loadState: LoadState){
        binding.swipeRefresh.isRefreshing = loadState is LoadState.Loading
    }

    private fun initAdapter() {
        binding.movieRecyclerview.adapter = mAdapter.withLoadStateFooter(
            footer = DefaultLoadStateAdapter { mAdapter.retry()}
        )
        binding.movieRecyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun observeMovies() {
        lifecycleScope.launchWhenStarted {
            vm.movies.collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

}


