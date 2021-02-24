package com.burachevsky.rssfeedreader.ui.adapters

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.burachevsky.rssfeedreader.R
import com.burachevsky.rssfeedreader.data.domainobjects.NewsItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@BindingAdapter("listData")
fun bindListData(recyclerView: RecyclerView, data: List<NewsItem>) {
    val adapter = recyclerView.adapter as NewsListAdapter
    adapter.submitList(data)
}

@BindingAdapter("setIconVisible")
fun bindSetIconVisible(channelIcon: ImageView, item: NewsItem) {
    Glide.with(channelIcon)
        .load(item.channel.logo)
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(R.drawable.ic_feed)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(channelIcon)
}