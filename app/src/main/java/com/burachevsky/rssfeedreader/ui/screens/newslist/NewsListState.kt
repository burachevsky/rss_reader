package com.burachevsky.rssfeedreader.ui.screens.newslist

import android.content.Context
import com.burachevsky.rssfeedreader.R
import com.burachevsky.rssfeedreader.data.domainobjects.NewsFeed
import com.burachevsky.rssfeedreader.data.domainobjects.NewsItem

data class NewsListState(
    val isInitializing: Boolean = false,
    val isRefreshing: Boolean = false,
    val data: List<NewsItem> = emptyList(),
    val feeds: List<NewsFeed> = emptyList(),
    val filter: ListFilter = All
) {
    fun asString(): String {
        return "state(isInitializing=$isInitializing, isRefreshing=$isRefreshing, dataSize=${data.size}, feedsSize=${feeds.size}, filter='${filter::class.simpleName}')"
    }
}


sealed class ListFilter {
    abstract fun titleString(context: Context): String
    abstract operator fun invoke(items: List<NewsItem>): List<NewsItem>
}

object All : ListFilter() {
    override fun titleString(context: Context): String {
        return context.getString(R.string.all_news)
    }

    override operator fun invoke(items: List<NewsItem>): List<NewsItem> {
        return items
    }
}

object Favorites : ListFilter() {
    override fun titleString(context: Context): String {
        return context.getString(R.string.favorite_news)
    }

    override operator fun invoke(items: List<NewsItem>): List<NewsItem> {
        return items.filter { it.isInCollection }
    }
}
class ByFeed(val feed: NewsFeed) : ListFilter() {
    override fun titleString(context: Context): String {
        return feed.channel.title
    }

    override operator fun invoke(items: List<NewsItem>): List<NewsItem> {
        return items.filter {
            it.channel.feedUrl == feed.channel.feedUrl
        }
    }
}