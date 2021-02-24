package com.burachevsky.rssfeedreader.ui.screens.newsdetail

import android.content.Context

sealed class NewsDetailsAction

object LikeItem: NewsDetailsAction()
object UnlikeItem: NewsDetailsAction()
data class OpenInBrowser(val ctx: Context): NewsDetailsAction()
data class ShareItem(val ctx: Context): NewsDetailsAction()
