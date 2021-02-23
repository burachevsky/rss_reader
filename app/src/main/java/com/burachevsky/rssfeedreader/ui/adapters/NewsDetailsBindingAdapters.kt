package com.burachevsky.rssfeedreader.ui.adapters

import android.text.format.DateFormat
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.burachevsky.rssfeedreader.R
import com.burachevsky.rssfeedreader.data.domainobjects.NewsItem
import com.burachevsky.rssfeedreader.utils.GlideImageGetter

@BindingAdapter("renderHtml")
fun bindRenderHtml(textView: TextView, html: String) {
    textView.text = HtmlCompat.fromHtml(
        html,
        HtmlCompat.FROM_HTML_MODE_COMPACT,
        GlideImageGetter(textView),
        { _, _, _, _ -> }
    )
}

@BindingAdapter("setAuthorAndDate")
fun bindSetAuthorAndDate(textView: TextView, item: NewsItem) {
    textView.text = textView.context.getString(
        R.string.by,
        item.author ?: "_",
        DateFormat.format("MMM dd hh:mm", item.pubDate)
    )
}

@BindingAdapter("setCategories")
fun bindSetCategories(textView: TextView, categories: List<String>) {
    textView.text = categories.joinToString(", ")
}