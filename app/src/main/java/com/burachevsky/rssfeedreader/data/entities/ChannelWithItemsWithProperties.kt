package com.burachevsky.rssfeedreader.data.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.burachevsky.rssfeedreader.data.domainobjects.FeedWithItems
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

fun ChannelWithItemsWithProperties.asDomain(): FeedWithItems {
    val channel = this.channel.asDomain()
    return FeedWithItems(
        channel,
        items.map { itemWithProperties ->
            val item = itemWithProperties.item
            NewsItem(
                feed = channel,
                title = item.title,
                description = item.description,
                pubDate = item.pubDate,
                itemLink = item.itemLink,
                categories = itemWithProperties.categories.map { it.name },
                author = item.author,
                isInCollection = itemWithProperties.favorite != null,
                isRead = itemWithProperties.read != null
            )
        }
    )
}