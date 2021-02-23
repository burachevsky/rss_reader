package com.burachevsky.rssfeedreader.ui.screens.newslist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.burachevsky.rssfeedreader.R
import com.burachevsky.rssfeedreader.ui.adapters.NewsListAdapter
import com.burachevsky.rssfeedreader.databinding.FragmentNewsListBinding
import com.burachevsky.rssfeedreader.data.domainobjects.NewsItem
import com.burachevsky.rssfeedreader.ui.base.Renderer
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class NewsListFragment : Fragment(),
    Renderer<NewsListState, NewsListEffect, NewsListAction> {

    private val viewModel: NewsListViewModel by viewModels()

    private lateinit var newsAdapter: NewsListAdapter

    private lateinit var binding: FragmentNewsListBinding

    private var job: Job? = null

    //private var scrollSavedState: Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.d(TAG, "onCreateView()")


        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_news_list, container, false
        )



        newsAdapter = NewsListAdapter(viewModel)

        binding.lifecycleOwner = this

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            handleOptionsMenuItemSelected(menuItem.itemId, viewModel, requireActivity())
        }

        binding.viewModel = viewModel

        binding.refreshLayout.setOnRefreshListener(viewModel::refreshNews)

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = newsAdapter
        newsAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW


        job = lifecycleScope.launch {
            launch {
                viewModel.pendingEffect.collect(::renderEffect)
            }
            launch {
                viewModel.state.collect(::renderState)
            }

            viewModel.subscribe()
        }

        setHasOptionsMenu(true)

        return binding.root
    }


    override fun renderState(state: NewsListState) {
        Log.d(TAG, "rendering state: ${state.asString()}")
        lifecycleScope.launch {
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

            is ShowDetails -> with (effect) {
                navigateToDetails(item)
            }

            is ShowNewsItemMenu -> with(effect) {
                showNewsItemMenu(view, item, viewModel)
            }
        }
    }

    private fun navigateToDetails(item: NewsItem?) {
        if (item != null) {
            findNavController().navigate(
                NewsListFragmentDirections
                    .actionNewsFragmentToNewsDetailsFragment(item)
            )
        }
    }

    override fun onResume() {
        Log.d(TAG, "onResume()")
        /*binding.recyclerView.layoutManager?.apply {
            onRestoreInstanceState(scrollSavedState)
            onLayoutChildren(binding.recyclerView.Recycler(), RecyclerView.State())
        }*/
        /* if (scrollSavedState != null) {
             val state = scrollSavedState as LinearLayoutManager.SavedState
             val mAnchorPosition = state::class.java
                 .getDeclaredField("mAnchorPosition")
             mAnchorPosition.isAccessible = true
             val position = mAnchorPosition.get(state) as Int
             binding.recyclerView.layoutManager?.scrollToPosition(position)

         }*/
        super.onResume()
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView()")
        job?.cancel()
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        super.onDestroy()
    }

    override fun onPause() {
        Log.d(TAG, "onPause()")
        super.onPause()
    }

    override fun onStart() {
        Log.d(TAG, "onStart()")
        super.onStart()
    }

    override fun onStop() {
        Log.d(TAG, "onStop()")
        super.onStop()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate()")
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach()")
        super.onAttach(context)
    }

    override fun onDetach() {
        Log.d("news:NewsListFragment", "onDetach()")
        super.onDetach()
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("news:NewsListFragment", "onViewCreated()")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("news:NewsListFragment", "onActivityCreated()")
        super.onActivityCreated(savedInstanceState)
    }


    companion object {
        val TAG = "news:NewsListFragment"
    }
}