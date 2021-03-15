package com.burachevsky.rssfeedreader.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.burachevsky.rssfeedreader.data.domainobjects.NewsItem
import java.util.*

@Entity(tableName = "news_items")
data class NewsItemEntity(
    @PrimaryKey(autoGenerate = false)
    val itemId: Int,
    val itemLink: String,
    val title: String,
    val description: String,
    val pubDate: Date,
    val author: String?,
    val channelFeedUrl: String,
)

fun List<NewsItem>.asEntities(): List<NewsItemEntity> {
    return map {
        NewsItemEntity(
            it.itemLink.hashCode(),
            it.itemLink, it.title, it.description,
            it.pubDate, it.author,
            it.feed.feedUrl
        )
    }
}