package com.burachevsky.rssfeedreader.utils.parser

import com.burachevsky.rssfeedreader.data.domainobjects.FeedWithItems
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

interface RssParser {

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(
        inputStream: InputStream,
        feedUrl: String
    ): FeedWithItems

    companion object {
        fun create() = RssParserImpl()
    }
}