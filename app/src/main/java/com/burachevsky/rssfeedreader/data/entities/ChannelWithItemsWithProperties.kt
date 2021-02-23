package com.burachevsky.rssfeedreader.data.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.burachevsky.rssfeedreader.data.domainobjects.NewsFeed
import com.burachevsky.rssfeedreader.data.domainobjects.NewsItem

data class ChannelWithItemsWithProperties(
    @Embedded
    val channel: NewsChannelEntity,
    @Relation(
        parentColumn = "feedUrl",
        entityColumn = "channelFeedUrl",
        entity = NewsItemEntity::class
    )
    val items: List<ItemWithProperties>
)


fun ChannelWithItemsWithProperties.asDomainItemList(): List<NewsItem> {
    val channel = this.channel.asDomain()

    return items.map { itemWithProperties ->
        val item = itemWithProperties.item
        NewsItem(
            channel = channel,
            title = item.title,
            description = item.description,
            pubDate = item.pubDate,
            itemLink = item.itemLink,
            categories = item.categories,
            author = item.author,
            isInCollection = itemWithProperties.favorite != null,
            isRead = itemWithProperties.read != null
        )
    }
}

fun ChannelWithItemsWithProperties.asDomain(): NewsFeed {
    val channel = this.channel.asDomain()
    return NewsFeed(
        channel,
        items.map { itemWithProperties ->
            val item = itemWithProperties.item
            NewsItem(
                channel = channel,
                title = item.title,
                description = item.description,
                pubDate = item.pubDate,
                itemLink = item.itemLink,
                categories = item.categories,
                author = item.author,
                isInCollection = itemWithProperties.favorite != null,
                isRead = itemWithProperties.read != null
            )
        }
    )
}