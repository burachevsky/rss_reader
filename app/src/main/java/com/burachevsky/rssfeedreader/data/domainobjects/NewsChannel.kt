package com.burachevsky.rssfeedreader.data.domainobjects

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsChannel(
    val feedUrl: String,
    val title: String,
    val link: String,
    val description: String,
    val logo: String?
) : Parcelable