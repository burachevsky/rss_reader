package com.burachevsky.rssfeedreader.ui.screens.newslist

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.burachevsky.rssfeedreader.R
import com.burachevsky.rssfeedreader.data.domainobjects.NewsFeed
import com.burachevsky.rssfeedreader.data.domainobjects.NewsItem
import com.burachevsky.rssfeedreader.data.repositories.NewsRepository
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
) : ViewModel() {

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

    suspend fun subscribe() {
        Log.d(TAG, "subscribing")
        feedsFlow.collect {
            Log.d(TAG, "collecting feedsFlow")
            update(it)
        }
    }

    private fun update(feeds: List<NewsFeed>) {
        viewModelScope.launch {
            Log.d(TAG, "update news list")
            val allItems = withContext(Dispatchers.Default) {
                feeds.flatMap { it.items }.sortedByDescending { it.pubDate }
            }
            _state.value = _state.value.copy(
                isInitializing = false,
                isRefreshing = false,
                data = allItems,
                feeds = feeds
            )
        }
    }

    fun filterList(filter: ListFilter) {
        _state.value = _state.value.copy(filter = filter)
    }

    fun tryGetFeed(url: String) {
        viewModelScope.launch {

            val urlExists = withContext(Dispatchers.Default) {
                state.value.feeds.any {
                    it.channel.feedUrl == url
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

    fun refreshNews() {
        viewModelScope.launch {
            _state.value = state.value.copy(isRefreshing = true)
            runSafely {
                repository.fetchNewsFromFeeds(state.value.feeds)
            }
            _state.value = state.value.copy(isRefreshing = false)
        }
    }

    fun deleteFeed(feed: NewsFeed) {
        viewModelScope.launch {
            runSafely {
                repository.deleteFeed(feed)
            }
        }
    }

    fun findFeed(item: NewsItem): NewsFeed {
        return state.value.feeds.find { it.channel.feedUrl == item.channel.feedUrl }!!
    }

    fun setRead(item: NewsItem, value: Boolean) {
        if (item.isRead == value)
            return

        viewModelScope.launch {
            runSafely {
                repository.setRead(item, value)
            }
        }
    }

    fun setLiked(item: NewsItem?, value: Boolean) {
        if (item == null || item.isInCollection == value)
            return

        viewModelScope.launch {
            runSafely {
                repository.setLiked(item, value)
            }
        }
    }

    fun showItemDetails(newsItem: NewsItem?) {
        if (newsItem != null) {
            viewModelScope.launch {
                _pendingEffect.emit(ShowDetails(newsItem))
            }
        }
    }

    fun showItemMenu(view: View, item: NewsItem?) {
        if (item != null) {
            viewModelScope.launch {
                _pendingEffect.emit(ShowNewsItemMenu(view, item))
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

    companion object {
        val TAG = "news:${NewsListFragment::class.simpleName}"
    }
}
