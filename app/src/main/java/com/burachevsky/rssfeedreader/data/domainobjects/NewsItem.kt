package com.burachevsky.rssfeedreader.data.domainobjects

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class NewsItem(
    val feed: NewsFeed,
    val title: String,
    val description: String,
    val pubDate: Date,
    val itemLink: String,
    val categories: List<String>,
    val author: String?,
    var isInCollection: Boolean = false,
    var isRead: Boolean = false
) : Parcelable {
    val identifier = itemLink.hashCode()
}
