package com.burachevsky.rssfeedreader.ui.screens.newslist

import android.content.Context
import android.view.View
import com.burachevsky.rssfeedreader.data.domainobjects.NewsFeed
import com.burachevsky.rssfeedreader.data.domainobjects.NewsItem

sealed class NewsListAction

object RefreshNews: NewsListAction()
object ShowAll: NewsListAction()
object ShowFavorites: NewsListAction()
data class DownloadFeed(val url: String): NewsListAction()
data class ShowItemDetails(val view: View, val item: NewsItem): NewsListAction()
data class ShowItemMenu(val view: View, val item: NewsItem): NewsListAction()
data class MarkItemAsRead(val item: NewsItem): NewsListAction()
data class MarkItemAsUnread(val item: NewsItem): NewsListAction()
data class LikeItem(val item: NewsItem): NewsListAction()
data class UnlikeItem(val item: NewsItem): NewsListAction()
data class GoToFeed(val item: NewsItem): NewsListAction()
data class DeleteFeed(val item: NewsItem): NewsListAction()
data class OpenDetailsInBrowser(val item: NewsItem, val ctx: Context): NewsListAction()
data class ShareItem(val item: NewsItem, val ctx: Context): NewsListAction()