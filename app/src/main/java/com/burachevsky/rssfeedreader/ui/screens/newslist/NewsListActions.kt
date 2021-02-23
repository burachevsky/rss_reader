package com.burachevsky.rssfeedreader.ui.screens.newslist

import android.view.View
import com.burachevsky.rssfeedreader.data.domainobjects.NewsFeed
import com.burachevsky.rssfeedreader.data.domainobjects.NewsItem

sealed class NewsListAction

data class ShowItemDetails(val item: NewsItem): NewsListAction()
data class ShowItemMenu(val view: View, val item: NewsItem): NewsListAction()
data class MarkItemAsRead(val item: NewsItem): NewsListAction()
data class GoToFeed(val feed: NewsFeed): NewsListAction()
data class DeleteFeed(val feed: NewsFeed): NewsListAction()
data class OpenDetailsInBrowser(val item: NewsItem)
data class ShareItem(val item: NewsItem)