package com.burachevsky.rssfeedreader.ui.screens.newsdetail

import androidx.lifecycle.*
import com.burachevsky.rssfeedreader.data.domainobjects.NewsItem
import com.burachevsky.rssfeedreader.data.repositories.NewsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class NewsDetailsViewModel @AssistedInject constructor (
    private val repository: NewsRepository,
    @Assisted item: NewsItem
) : ViewModel() {

    private var _item: MutableLiveData<NewsItem> = MutableLiveData(item)
    val item: LiveData<NewsItem>
        get() = _item


    fun likeItem(newsItem: NewsItem?, value: Boolean) {
        if (newsItem != null) {
            viewModelScope.launch {
                repository.setLiked(newsItem, value)
            }
        }
    }

    fun readItem(newsItem: NewsItem?) {
        if (newsItem != null) {
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
}

@AssistedFactory
interface NewsDetailsViewModelFactory {
    fun create(item: NewsItem): NewsDetailsViewModel
}