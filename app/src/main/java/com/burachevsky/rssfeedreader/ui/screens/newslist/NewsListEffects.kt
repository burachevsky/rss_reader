package com.burachevsky.rssfeedreader.ui.screens.newslist

import android.view.View
import com.burachevsky.rssfeedreader.data.domainobjects.NewsItem

sealed class NewsListEffect

data class ShowSnackBarMessage(
    val resId: Int? = null,
    val string: String? = null
): NewsListEffect()
