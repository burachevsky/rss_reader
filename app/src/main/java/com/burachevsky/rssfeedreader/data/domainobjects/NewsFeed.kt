package com.burachevsky.rssfeedreader.data.domainobjects

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsFeed(
    val channel: NewsChannel,
    val items: List<NewsItem>
) : Parcelable
