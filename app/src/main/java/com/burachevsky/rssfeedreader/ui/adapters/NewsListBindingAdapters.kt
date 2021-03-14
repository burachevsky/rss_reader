package com.burachevsky.rssfeedreader.ui.adapters

import android.annotation.SuppressLint
import android.text.format.DateFormat
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
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

@BindingAdapter("formatDate")
fun bindFormatDate(textView: TextView, item: NewsItem) {
    textView.text = DateFormat.format("dd.MM hh:mm", item.pubDate)
}

@BindingAdapter("setTextColor")
fun bindSetTextColor(textView: TextView, item: NewsItem) {
    val ctx = textView.context
    textView.setTextColor(
        if (item.isRead)
            ContextCompat.getColor(ctx, R.color.read_text)
        else ContextCompat.getColor(ctx, R.color.unread_text)
    )
}
