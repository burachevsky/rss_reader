package com.burachevsky.rssfeedreader.ui.screens.newslist

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewModelScope
import com.burachevsky.rssfeedreader.R
import com.burachevsky.rssfeedreader.data.domainobjects.NewsItem
import com.burachevsky.rssfeedreader.ui.dialogs.AddDialogFragment
import com.burachevsky.rssfeedreader.ui.dialogs.FilterFeedsDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("RestrictedApi", "UseCompatLoadingForDrawables")
fun showNewsItemMenu(view: View, item: NewsItem, viewModel: NewsListViewModel) {
    val ctx = view.context
    val menu = PopupMenu(ctx, view)
    menu.inflate(R.menu.item_menu)
    menu.menu.findItem(R.id.markAs).apply {

        if (item.isRead) {
            icon = ctx.getDrawable(R.drawable.ic_mark_as_unread)
            title = ctx.getString(R.string.markAsUnread)
        } else {
            icon = ctx.getDrawable(R.drawable.ic_done)
            title = ctx.getString(R.string.markAsRead)
        }
    }

    menu.setOnMenuItemClickListener { menuItem ->
        when (menuItem.itemId) {
            R.id.showDetails -> {
                viewModel.showItemDetails(item)
            }

            R.id.markAs -> {
                viewModel.setRead(item, !item.isRead)
            }

            R.id.goToFeed -> {
                viewModel.apply {
                    viewModelScope.launch {
                        filterList(
                            ByFeed(
                                withContext(Dispatchers.Default) {
                                    findFeed(item)
                                }
                            )
                        )
                    }
                }
            }

            R.id.deleteFeed -> {
                viewModel.apply {
                    viewModelScope.launch {
                        deleteFeed(
                            withContext(Dispatchers.Default) {
                                findFeed(item)
                            }
                        )
                    }
                }
            }

            R.id.openInBrowser -> {
                ctx.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(item.itemLink))
                )
            }

            R.id.share -> {
                ctx.startActivity(
                    Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, item.itemLink)
                        type = "text/plain"
                    }
                )
            }
        }
        true
    }

    val menuPopupHelper = MenuPopupHelper(
        ctx,
        menu.menu as MenuBuilder,
        view
    )
    menuPopupHelper.setForceShowIcon(true)
    menuPopupHelper.show()
}

fun handleOptionsMenuItemSelected(
    itemId: Int,
    viewModel: NewsListViewModel,
    activity: FragmentActivity
): Boolean {
    return when (itemId) {

        R.id.addFeed -> {
            viewModel.viewModelScope.launch {
                val addDialog = withContext(Dispatchers.Default) {
                    AddDialogFragment(viewModel)
                }
                addDialog.show(activity.supportFragmentManager, "AddDialogFragment")
            }
            true
        }

        R.id.refresh -> {
            viewModel.refreshNews()
            true
        }

        R.id.filter -> {
            val filterDialog = FilterFeedsDialogFragment(viewModel)
            filterDialog.show(activity.supportFragmentManager, "FilterFeedsDialog")
            true
        }

        else -> false
    }
}