package com.burachevsky.rssfeedreader.ui.screens.newslist

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.burachevsky.rssfeedreader.R
import com.burachevsky.rssfeedreader.ui.adapters.NewsListAdapter
import com.burachevsky.rssfeedreader.databinding.FragmentNewsListBinding
import com.burachevsky.rssfeedreader.ui.base.Renderer
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class NewsListFragment : Fragment(),
    Renderer<NewsListState, NewsListEffect> {

    private val newsListViewModel: NewsListViewModel by viewModels()

    private lateinit var newsAdapter: NewsListAdapter

    private lateinit var binding: FragmentNewsListBinding

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate()")
        newsAdapter = NewsListAdapter(newsListViewModel)
        newsAdapter.stateRestorationPolicy = RecyclerView
            .Adapter.StateRestorationPolicy.ALLOW
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.d(TAG, "onCreateView()")

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_news_list, container, false
        )

        setHasOptionsMenu(true)
        return binding.root
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated()")

        binding.apply {
            lifecycleOwner = this@NewsListFragment
            viewModel = this@NewsListFragment.newsListViewModel

            recyclerView.adapter = newsAdapter

            val menu = toolbar.menu
            if (menu is MenuBuilder) {
                menu.setOptionalIconsVisible(true)
            }

            toolbar.setOnMenuItemClickListener { menuItem ->
                handleOptionsMenuItemSelected(
                    menuItem.itemId,
                    newsListViewModel,
                    requireActivity()
                )
            }

            filterButton.setOnClickListener {
                newsListViewModel.submit(ShowFilterDialog(parentFragmentManager))
            }

            refreshLayout.setOnRefreshListener {
                newsListViewModel.submit(RefreshNews)
            }
        }

        job = viewLifecycleOwner.lifecycleScope.launch {
            launch {
                newsListViewModel.pendingEffect.collect(::renderEffect)
            }
            launch {
                newsListViewModel.state.collect(::renderState)
            }

            newsListViewModel.subscribe()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView()")
        job?.cancel()
        super.onDestroyView()
    }

    override fun renderState(state: NewsListState) {
        Log.d(TAG, "rendering state: ${state.asString()}")
        viewLifecycleOwner.lifecycleScope.launch {
            binding.apply {

                refreshLayout.isRefreshing = state.isRefreshing
                toolbar.title = state.filter.titleString(requireContext())

                if (state.isInitializing) {
                    loadingBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    emptyListView.visibility = View.GONE
                } else {
                    loadingBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE

                    val displayData = withContext(Dispatchers.Default) {
                        state.run { filter(data) }
                    }

                    if (displayData.isEmpty()) {
                        emptyListView.visibility = View.VISIBLE
                    } else {
                        emptyListView.visibility = View.GONE
                    }

                    newsAdapter.submitList(displayData)
                }
            }
        }
    }

    override fun renderEffect(effect: NewsListEffect) {
        Log.d(TAG, "renderEffect: ${effect::class.java.name}")
        when (effect) {
            is ShowSnackBarMessage -> with (effect) {
                val message = string ?: getString(resId!!)
                Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
                    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                    .show()
            }
        }
    }

    override fun onStart() {
        Log.d(TAG, "onStart()")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume()")
        super.onResume()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        super.onDestroy()
    }

    override fun onPause() {
        Log.d(TAG, "onPause()")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop()")
        super.onStop()
    }

    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach()")
        super.onAttach(context)
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach()")
        super.onDetach()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated()")
        super.onActivityCreated(savedInstanceState)
    }

    companion object {
        val TAG ="${NewsListFragment::class.simpleName}"
    }
}