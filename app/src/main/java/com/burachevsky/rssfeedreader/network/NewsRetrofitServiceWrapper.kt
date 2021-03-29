package com.burachevsky.rssfeedreader.network

import com.burachevsky.rssfeedreader.data.domainobjects.FeedWithItems
import com.burachevsky.rssfeedreader.utils.parser.RssParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsRetrofitServiceWrapper @Inject constructor(
    private val retrofitService: NewsRetrofitService,
    private val parser: RssParser
): NewsWebservice {

    override suspend fun getNewsFeed(url: String): FeedWithItems {
        val response = retrofitService.requestFeed(url)
        return withContext(Dispatchers.Default) {
            parser.parse(response.byteStream(), url)
        }
    }
}