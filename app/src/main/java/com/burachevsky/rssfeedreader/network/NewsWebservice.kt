package com.burachevsky.rssfeedreader.network

import com.burachevsky.rssfeedreader.data.domainobjects.NewsFeed

interface NewsWebservice {
    suspend fun getNewsFeed(url: String): NewsFeed
}
