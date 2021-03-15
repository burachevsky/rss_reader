package com.burachevsky.rssfeedreader.data.repositories

import android.util.Log
import com.burachevsky.rssfeedreader.data.AppDatabase
import com.burachevsky.rssfeedreader.data.daos.*
import com.burachevsky.rssfeedreader.data.entities.*
import com.burachevsky.rssfeedreader.data.domainobjects.FeedWithItems
import com.burachevsky.rssfeedreader.data.domainobjects.NewsFeed
import com.burachevsky.rssfeedreader.data.domainobjects.NewsItem
import com.burachevsky.rssfeedreader.network.NewsWebservice
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepository @Inject constructor (
    private val webservice: NewsWebservice,
    private val database: AppDatabase,
    private val newsChannelDao: NewsChannelDao,
    private val newsItemDao: NewsItemDao,
    private val favoriteItemDao: FavoriteItemDao,
    private val readItemDao: ReadItemDao,
    private val newsCategoryDao: NewsCategoryDao
) {

    fun observeFeeds(): Flow<List<FeedWithItems>> {
        return newsChannelDao
            .getChannelsWithItemsFlow()
            .map { entities -> entities.map { it.asDomain() } }
    }

    suspend fun setLiked(item: NewsItem, value: Boolean) {
        val favItem = FavoriteItem(item.itemLink)
        if (value)
            favoriteItemDao.insertFavoriteItem(favItem)
        else
            favoriteItemDao.deleteFavoriteItem(favItem)
    }

    suspend fun setRead(item: NewsItem, value: Boolean) {
        val readItem = ReadItem(item.itemLink)
        if (value)
            readItemDao.insertReadItem(readItem)
        else
            readItemDao.deleteReadItem(readItem)
    }

    suspend fun fetchFeed(url: String) {
        return withContext(Dispatchers.IO) {
            val feed = webservice.getNewsFeed(url)
            Log.d("NewsRepository", "items reveived: ${feed.items.size}")
            database.runInTransaction {
                runBlocking {
                    newsChannelDao.insertChannel(feed.feed.asEntity())
                    newsItemDao.insertItems(feed.items.asEntities())
                }
            }
        }
    }

    suspend fun fetchNewsFromFeeds(feeds: List<NewsFeed>) {
        return withContext(Dispatchers.IO) {
            val freshItems = feeds.flatMap {
                webservice.getNewsFeed(it.feedUrl)
                    .items
            }
            newsItemDao.insertItems(freshItems.asEntities())
        }
    }

    suspend fun deleteFeed(feedWithItems: FeedWithItems) {
        withContext(Dispatchers.IO) {
            database.runInTransaction {
                runBlocking {
                    newsChannelDao.deleteChannel(feedWithItems.feed.asEntity())
                    newsItemDao.deleteItemsFromFeed(feedWithItems.feed.feedUrl)
                    feedWithItems.items.forEach { item ->
                        setRead(item, false)
                        setLiked(item, false)
                    }
                }
            }
        }
    }
}
