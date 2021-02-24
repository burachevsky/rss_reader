package com.burachevsky.rssfeedreader.data.domainobjects

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeedWithItems(
    val feed: NewsFeed,
    val items: List<NewsItem>
) : Parcelable
