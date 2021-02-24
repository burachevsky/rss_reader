package com.burachevsky.rssfeedreader.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.burachevsky.rssfeedreader.data.domainobjects.NewsFeed

@Entity(tableName = "news_channels")
data class NewsChannelEntity(
    @PrimaryKey
    val feedUrl: String,
    val title: String,
    val link: String,
    val description: String,
    val logo: String?
)

fun NewsChannelEntity.asDomain(): NewsFeed {
    return NewsFeed(feedUrl, title, link, description, logo)
}

fun NewsFeed.asEntity(): NewsChannelEntity {
    return NewsChannelEntity(feedUrl, title, link, description, logo)
}