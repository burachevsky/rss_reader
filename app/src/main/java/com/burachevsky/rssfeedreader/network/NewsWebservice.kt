package com.burachevsky.rssfeedreader.network

import com.burachevsky.rssfeedreader.data.domainobjects.FeedWithItems

interface NewsWebservice {
    suspend fun getNewsFeed(url: String): FeedWithItems
}
