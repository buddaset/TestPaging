package com.example.test_paging.presentation.repo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test_paging.Injection
import com.example.test_paging.R
import com.example.test_paging.ViewModelFactory
import com.example.test_paging.databinding.FragmentRepoBinding
import com.example.test_paging.databinding.RepoViewItemBinding
import com.example.test_paging.presentation.movie.adapter.state.DefaultLoadStateAdapter
import com.example.test_paging.presentation.repo.repoadapter.RepoAdapter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class RepoFragment : Fragment() {

    private lateinit var binding: FragmentRepoBinding

    private val vm: RepoViewModel by viewModels {
        Injection.provideViewModelFactory(requireActivity().applicationContext)
    }

    private val repoAdapter by lazy { RepoAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_repo, container, false)
        binding = FragmentRepoBinding.bind(view)
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
            repoAdapter.refresh()
        }
        repoAdapter.loadStateFlow
            .debounce(300)
            .onEach { refreshLoadState(it.refresh)}
            .launchIn(lifecycleScope)
    }

    private fun refreshLoadState(loadState: LoadState){
        binding.swipeRefresh.isRefreshing = loadState is LoadState.Loading
    }


    private fun initAdapter() {
        binding.recyclerViewRepo.adapter = repoAdapter.withLoadStateFooter(
            footer = DefaultLoadStateAdapter { repoAdapter.retry()}
        )
        binding.recyclerViewRepo.layoutManager = LinearLayoutManager( requireContext())
    }

    private fun observeMovies() {
        lifecycleScope.launchWhenStarted {
            vm.repos.collectLatest {
                repoAdapter.submitData(it)
            }
        }
    }

}