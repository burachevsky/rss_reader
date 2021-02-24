package com.burachevsky.rssfeedreader.ui.screens.newsdetail

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.*
import com.burachevsky.rssfeedreader.data.domainobjects.NewsItem
import com.burachevsky.rssfeedreader.data.repositories.NewsRepository
import com.burachevsky.rssfeedreader.ui.base.Actioner
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsDetailsViewModel @AssistedInject constructor (
    private val repository: NewsRepository,
    @Assisted item: NewsItem
) : ViewModel(), Actioner<NewsDetailsAction> {

    private var _state = MutableStateFlow(NewsDetailsState(item = item))
    val state: StateFlow<NewsDetailsState>
        get() = _state

    val item: NewsItem
        get() = state.value.item

    init {
        readItem(item)
    }

    private fun likeItem(newsItem: NewsItem?, value: Boolean) {
        if (newsItem != null && newsItem.isInCollection != value) {
            viewModelScope.launch {
                repository.setLiked(newsItem, value)
                _state.value = _state.value.copy(
                    item = newsItem.copy(isInCollection = value)
                )
            }
        }
    }

    private fun readItem(newsItem: NewsItem?) {
        if (newsItem != null && !newsItem.isRead) {
            viewModelScope.launch {
                if (!newsItem.isRead)
                    repository.setRead(newsItem, true)
            }
        }
    }

    companion object {
        fun provideFactory(
            assistedFactory: NewsDetailsViewModelFactory,
            item: NewsItem
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(item) as T
            }
        }
    }

    override fun submit(action: NewsDetailsAction) {
        when (action) {

            LikeItem -> likeItem(item, true)

            UnlikeItem -> likeItem(item, false)

            is OpenInBrowser -> with (action) {
                runSafely {
                    ctx.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(item.itemLink))
                    )
                }
            }

            is ShareItem -> with (action) {
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

    private inline fun runSafely(block: () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@AssistedFactory
interface NewsDetailsViewModelFactory {
    fun create(item: NewsItem): NewsDetailsViewModel
}