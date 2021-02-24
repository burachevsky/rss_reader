package com.burachevsky.rssfeedreader.ui.screens.newslist

import android.annotation.SuppressLint
import android.view.View
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import com.burachevsky.rssfeedreader.R
import com.burachevsky.rssfeedreader.data.domainobjects.NewsItem

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
            R.id.showDetails -> viewModel.submit(
                ShowItemDetails(view, item)
            )

            R.id.markAs -> viewModel.submit(
                if (item.isRead) MarkItemAsUnread(item)
                else MarkItemAsRead(item)
            )

            R.id.goToFeed -> viewModel.submit(
                GoToFeed(item.feed)
            )

            R.id.deleteFeed -> viewModel.submit(
                DeleteFeed(item.feed)
            )

            R.id.openInBrowser -> viewModel.submit(
                OpenDetailsInBrowser(item, ctx)
            )
            R.id.share -> viewModel.submit(
                ShareItem(item, ctx)
            )
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
            viewModel.submit(ShowAddDialog(activity.supportFragmentManager))
            true
        }

        R.id.refresh -> {
            viewModel.submit(RefreshNews)
            true
        }

        R.id.filter -> {
            viewModel.submit(ShowFilterDialog(activity.supportFragmentManager))
            true
        }

        else -> false
    }
}