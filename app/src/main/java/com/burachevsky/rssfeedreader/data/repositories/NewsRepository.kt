package com.burachevsky.rssfeedreader.data.repositories

import android.util.Log
import com.burachevsky.rssfeedreader.data.AppDatabase
import com.burachevsky.rssfeedreader.data.daos.FavoriteItemDao
import com.burachevsky.rssfeedreader.data.daos.NewsChannelDao
import com.burachevsky.rssfeedreader.data.daos.NewsItemDao
import com.burachevsky.rssfeedreader.data.daos.ReadItemDao
import com.burachevsky.rssfeedreader.data.entities.*
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
    private val readItemDao: ReadItemDao
) {

    fun observeFeeds(): Flow<List<NewsFeed>> {
        return newsChannelDao
            .getChannelsWithItemsFlow()
            .map { entities -> entities.map { it.asDomain() } }
    }

    fun observeReadItems(): Flow<List<String>> {
        return readItemDao.getReadItemsFlow()
    }

    fun observeFavoriteItems(): Flow<List<String>> {
        return favoriteItemDao.getFavoritesFlow()
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

    suspend fun fetchFeed(url: String): NewsFeed {
        return withContext(Dispatchers.IO) {
            val feed = webservice.getNewsFeed(url)
            Log.d("NewsRepository", "items reveived: ${feed.items.size}")
            database.runInTransaction {
                runBlocking {
                    newsChannelDao.insertChannel(feed.channel.asEntity())
                    newsItemDao.insertItems(feed.items.asEntities())
                }
            }
            feed
        }
    }

    suspend fun fetchNewsFromFeeds(feeds: List<NewsFeed>): List<NewsItem> {
        return withContext(Dispatchers.IO) {
            val freshItems = feeds.flatMap {
                webservice.getNewsFeed(it.channel.feedUrl)
                    .items
            }
            newsItemDao.insertItems(freshItems.asEntities())
            freshItems
        }
    }

    suspend fun deleteFeed(feed: NewsFeed) {
        withContext(Dispatchers.IO) {
            database.runInTransaction {
                runBlocking {
                    newsChannelDao.deleteChannel(feed.channel.asEntity())
                    newsItemDao.deleteItemsFromFeed(feed.channel.feedUrl)
                    feed.items.forEach { item ->
                        setRead(item, false)
                        setLiked(item, false)
                    }
                }
            }
        }
    }
}
