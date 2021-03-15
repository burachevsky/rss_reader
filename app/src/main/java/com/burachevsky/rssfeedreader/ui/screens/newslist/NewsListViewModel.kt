package com.burachevsky.rssfeedreader.ui.screens.newslist

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.burachevsky.rssfeedreader.R
import com.burachevsky.rssfeedreader.data.domainobjects.FeedWithItems
import com.burachevsky.rssfeedreader.data.domainobjects.NewsFeed
import com.burachevsky.rssfeedreader.data.domainobjects.NewsItem
import com.burachevsky.rssfeedreader.data.repositories.NewsRepository
import com.burachevsky.rssfeedreader.ui.base.Actioner
import com.burachevsky.rssfeedreader.ui.dialogs.AddDialogFragment
import com.burachevsky.rssfeedreader.ui.dialogs.FilterFeedsDialogFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.xmlpull.v1.XmlPullParserException
import java.net.MalformedURLException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel(), Actioner<NewsListAction> {

    init {
        Log.d(TAG, "viewModel created")
    }

    private val _pendingEffect = MutableSharedFlow<NewsListEffect>()
    val pendingEffect: SharedFlow<NewsListEffect>
        get() = _pendingEffect

    private val _state = MutableStateFlow(NewsListState(isInitializing = true))
    val state: StateFlow<NewsListState>
        get() = _state

    private val feedsFlow = repository.observeFeeds()

    private var job: Job? = null

    fun subscribe(
        scope: CoroutineScope,
        stateRenderer: (NewsListState) -> Unit,
        effectRenderer: (NewsListEffect) -> Unit
    ) {
        Log.d(TAG, "subscribing")

        job = scope.launch {
            launch {
                state.collect { stateRenderer(it) }
            }
            launch {
                pendingEffect.collect { effectRenderer(it) }
            }

            if (!_state.value.isInitializing)
                _state.emit(_state.value.copy(isInitializing = true))

            feedsFlow.collect {
                Log.d(TAG, "collecting feedsFlow")
                update(it)
            }
        }
    }

    fun unsubscribe() {
        Log.d(TAG, "unsubscribing")
        job?.cancel()
    }

    private fun update(feedWithItems: List<FeedWithItems>) {
        viewModelScope.launch {
            Log.d(TAG, "update news list")
            val allItems = withContext(Dispatchers.Default) {
                feedWithItems.flatMap { it.items }.sortedByDescending { it.pubDate }
            }
            val allFeeds = withContext(Dispatchers.Default) {
                feedWithItems.map { it.feed }
            }
            _state.value = _state.value.copy(
                isInitializing = false,
                isRefreshing = false,
                data = allItems,
                feeds = allFeeds
            )
        }
    }

    private fun filterList(filter: ListFilter) {
        _state.value = _state.value.copy(filter = filter)
    }

    private fun tryGetFeed(url: String) {
        viewModelScope.launch {

            val urlExists = withContext(Dispatchers.Default) {
                state.value.feeds.any {
                    it.feedUrl == url
                }
            }

            if (urlExists) {
                _pendingEffect.emit(
                    ShowSnackBarMessage(R.string.feed_already_exists)
                )
                return@launch
            }

            _state.value = state.value.copy(isRefreshing = true)
            runSafely {
                repository.fetchFeed(url)
            }
            _state.value = state.value.copy(isRefreshing = false)
        }
    }

    private fun refreshNews() {
        viewModelScope.launch {
            _state.value = state.value.copy(isRefreshing = true)
            runSafely {
                repository.fetchNewsFromFeeds(state.value.feeds)
            }
            _state.value = state.value.copy(isRefreshing = false)
        }
    }

    private fun deleteFeed(feed: NewsFeed) {
        viewModelScope.launch {
            runSafely {
                repository.deleteFeed(
                    findFeedWithItems(feed)
                )
            }
        }
    }

    private suspend fun findFeedWithItems(feed: NewsFeed): FeedWithItems {
        return withContext(Dispatchers.Default) {
            FeedWithItems(
                feed,
                _state.value.data.filter { it.feed.feedUrl == feed.feedUrl }
            )
        }
    }

    private fun setRead(item: NewsItem, value: Boolean) {
        if (item.isRead == value)
            return

        viewModelScope.launch {
            runSafely {
                repository.setRead(item, value)
            }
        }
    }

    private fun setLiked(item: NewsItem?, value: Boolean) {
        if (item == null || item.isInCollection == value)
            return

        viewModelScope.launch {
            runSafely {
                repository.setLiked(item, value)
            }
        }
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared()")
        super.onCleared()
    }

    private inline fun runSafely(block: () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            Log.i("news:NewsListViewModel", "$e")
            viewModelScope.launch {
                _pendingEffect.emit(
                    when (e) {
                        is MalformedURLException -> ShowSnackBarMessage(R.string.invalid_url)
                        is UnknownHostException -> ShowSnackBarMessage(R.string.unable_to_connect)
                        is XmlPullParserException -> ShowSnackBarMessage(R.string.invalid_rss_feed)
                        else -> {
                            e.printStackTrace()
                            ShowSnackBarMessage(string = "$e")
                        }
                    }
                )
            }
        }
    }

    override fun submit(action: NewsListAction) {
        when (action) {
            RefreshNews -> refreshNews()

            ShowAll -> filterList(All)

            ShowFavorites -> filterList(Favorites)

            is ShowAddDialog -> with(action) {
                viewModelScope.launch {
                    val addDialog = withContext(Dispatchers.Default) {
                        AddDialogFragment(this@NewsListViewModel)
                    }
                    addDialog.show(fragmentManager, "AddDialogFragment")
                }
            }

            is ShowFilterDialog -> with(action) {
                viewModelScope.launch {
                    val filterDialog = FilterFeedsDialogFragment(this@NewsListViewModel)
                    filterDialog.show(fragmentManager, "FilterFeedsDialog")
                }
            }

            is DownloadFeed -> with(action) {
                tryGetFeed(url)
            }

            is ShowItemDetails -> with(action) {
                view.findNavController().navigate(
                    NewsListFragmentDirections
                        .actionNewsFragmentToNewsDetailsFragment(item)
                )
            }

            is ShowItemMenu -> with(action) {
                showNewsItemMenu(view, item, this@NewsListViewModel)
            }

            is MarkItemAsRead -> with(action) {
                setRead(item, true)
            }

            is MarkItemAsUnread -> with(action) {
                setRead(item, false)
            }

            is LikeItem -> with(action) {
                setLiked(item, true)
            }

            is UnlikeItem -> with(action) {
                setLiked(item, false)
            }

            is GoToFeed -> with(action) {
                viewModelScope.launch {
                    filterList(ByFeed(feed))
                }
            }

            is DeleteFeed -> with(action) {
                viewModelScope.launch {
                    deleteFeed(feed)
                }
            }

            is OpenDetailsInBrowser -> with(action) {
                runSafely {
                    ctx.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(item.itemLink))
                    )
                }
            }

            is ShareItem -> with(action) {
                runSafely {
                    ctx.startActivity(
                        Intent().apply {
                            this.action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, item.itemLink)
                            type = "text/plain"
                        }
                    )
                }
            }
        }
    }

    companion object {
        val TAG = "${NewsListViewModel::class.simpleName}"
    }
}
