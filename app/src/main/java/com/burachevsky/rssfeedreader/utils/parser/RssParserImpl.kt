package com.burachevsky.rssfeedreader.utils.parser

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

import com.burachevsky.rssfeedreader.data.domainobjects.NewsFeed
import com.burachevsky.rssfeedreader.data.domainobjects.FeedWithItems
import com.burachevsky.rssfeedreader.data.domainobjects.NewsItem
import javax.inject.Inject

class RssParserImpl @Inject constructor() : RssParser {

    @Throws(XmlPullParserException::class, IOException::class)
    override fun parse(inputStream: InputStream, feedUrl: String): FeedWithItems {
        return inputStream.use { `in` ->
            Xml.newPullParser().run {
                setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                setInput(`in`, null)
                nextTag()
                readRss(feedUrl)
            }
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun XmlPullParser.readRss(feedUrl: String): FeedWithItems {
        require(XmlPullParser.START_TAG, null, "rss")
        var feedWithItems: FeedWithItems? = null

        while (next() != XmlPullParser.END_TAG) {
            if (eventType != XmlPullParser.START_TAG)
                continue

            when (name) {
                "channel" -> feedWithItems = readFeed(feedUrl)
                else -> skip()
            }
        }

        return feedWithItems ?: throw XmlPullParserException("null channel")
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun XmlPullParser.readFeed(feedUrl: String): FeedWithItems {

        require(XmlPullParser.START_TAG, null, "channel")

        var title: String? = null
        var link: String? = null
        var description: String? = null
        var logo: String? = null
        val itemGenerators: MutableList<(NewsFeed) -> NewsItem> = ArrayList()

        while (next() != XmlPullParser.END_TAG) {
            if (eventType != XmlPullParser.START_TAG)
                continue

            when (name) {
                "title" -> title = readContent("title")
                "link" -> link = readContent("link")
                "description" -> description = readContent("description")
                "item" -> itemGenerators.add(readItem())
                "webfeeds:logo" -> logo = readContent("webfeeds:logo")
                "image" -> logo = readImage()
                else -> skip()
            }
        }

        val channel = NewsFeed(
            title = title ?: throw XmlPullParserException("null title"),
            link = link ?: throw XmlPullParserException("null link"),
            description = description ?: throw XmlPullParserException("null description"),
            feedUrl = feedUrl,
            logo = logo
        )

        return FeedWithItems(channel, itemGenerators.map { it(channel) })
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun XmlPullParser.readItem(): (NewsFeed) -> NewsItem {
        require(XmlPullParser.START_TAG, null, "item")

        var title: String? = null
        var description: String? = null
        var link: String? = null
        var pubDate: Date? = null
        val categories: MutableList<String> = ArrayList()
        var author: String? = null

        while (next() != XmlPullParser.END_TAG) {
            if (eventType != XmlPullParser.START_TAG)
                continue

            when (name) {
                "title" -> title = readContent("title")
                "description" -> description = readContent("description")
                "link" -> link = readContent("link")
                "pubDate" -> pubDate = readContent("pubDate") { it.toDate() }
                "category" -> categories.add(readContent("category"))
                "author" -> author = readContent("author")
                "dc:creator" -> author = readContent("dc:creator")
                else -> skip()
            }
        }

        return { chan ->
            NewsItem(
                title = title ?: throw XmlPullParserException("null title"),
                description = description ?: throw XmlPullParserException("null description"),
                itemLink = link ?: throw XmlPullParserException("null link"),
                pubDate = pubDate ?: throw XmlPullParserException("null pubDate"),
                categories = categories,
                author = author,
                feed = chan
            )
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun XmlPullParser.readImage(): String? {
        require(XmlPullParser.START_TAG, null, "image")

        var url: String? = null

        while (next() != XmlPullParser.END_TAG) {
            if (eventType != XmlPullParser.START_TAG)
                continue

            when (name) {
                "url" -> url = readContent("url")
                else -> skip()
            }
        }

        return url
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun XmlPullParser.readContent(
        tag: String,
    ): String {
        return readContent(tag) { str -> str }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun <T> XmlPullParser.readContent(
        tag: String,
        mapFun: ((String) -> T)
    ): T {
        require(XmlPullParser.START_TAG, null, tag)
        val content = nextText()
        require(XmlPullParser.END_TAG, null, tag)
        return mapFun(content)
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun XmlPullParser.skip() {
        if (eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (next()) {
                XmlPullParser.END_TAG -> --depth
                XmlPullParser.START_TAG -> ++depth
            }
        }
    }

    private fun String.toDate(): Date = this.toDate("EEE, dd MMM yyyy HH:mm:ss Z")

    @Throws(XmlPullParserException::class)
    private fun String.toDate(format: String): Date {
        val pos = ParsePosition(0)
        val simpleDateFormat = SimpleDateFormat(format, Locale.US)
        return simpleDateFormat.parse(this, pos)
            ?: throw XmlPullParserException("invalid date")
    }
}