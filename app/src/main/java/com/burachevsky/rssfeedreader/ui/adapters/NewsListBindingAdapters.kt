package com.burachevsky.rssfeedreader.ui.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.burachevsky.rssfeedreader.R
import com.burachevsky.rssfeedreader.data.domainobjects.NewsItem

@BindingAdapter("setIconVisible")
fun bindSetIconVisible(channelIcon: ImageView, item: NewsItem) {
    Glide.with(channelIcon)
        .load(item.feed.logo)
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(R.drawable.ic_feed)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(channelIcon)
}