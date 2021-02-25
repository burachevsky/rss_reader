package com.burachevsky.rssfeedreader.network

import com.burachevsky.rssfeedreader.data.domainobjects.FeedWithItems
import com.burachevsky.rssfeedreader.utils.parser.RssParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsWebserviceImpl @Inject constructor(
    private val parser: RssParser
) : NewsWebservice {

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun getNewsFeed(url: String): FeedWithItems =
        withContext(Dispatchers.IO) {
            val input = downloadUrl(url) ?: throw IOException("never happens")
            withContext(Dispatchers.Default) {
                input.use {
                    parser.parse(input, url)
                }
            }
        }

    @Throws(IOException::class)
    private fun downloadUrl(urlString: String): InputStream? {
        val url = URL(urlString)
        return (url.openConnection() as? HttpURLConnection)?.run {
            readTimeout = 10000
            connectTimeout = 15000
            requestMethod = "GET"
            doInput = true
            connect()
            inputStream
        }
    }
}