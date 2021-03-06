package com.burachevsky.rssfeedreader.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.burachevsky.rssfeedreader.R
import com.burachevsky.rssfeedreader.databinding.ListItemFeedBinding
import com.burachevsky.rssfeedreader.data.domainobjects.NewsFeed
import com.burachevsky.rssfeedreader.ui.screens.newslist.DeleteFeed
import com.burachevsky.rssfeedreader.ui.screens.newslist.GoToFeed
import com.burachevsky.rssfeedreader.ui.screens.newslist.NewsListViewModel

class FeedsListAdapter(
    private val viewModel: NewsListViewModel,
    private val onSelectedCallback: () -> Unit
) : ListAdapter<NewsFeed, FeedsListAdapter.ViewHolder>(
    DiffCallback
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_feed,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            viewModel.submit(GoToFeed(item))
            onSelectedCallback()
        }
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: ListItemFeedBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(feed: NewsFeed) {
            binding.apply {
                title.text = feed.title
                imageButton.setOnClickListener {
                    viewModel.submit(DeleteFeed(feed))
                }
                Glide.with(binding.root)
                    .load(feed.logo)
                    .error(R.drawable.ic_feed)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(channelIcon)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<NewsFeed>() {
        override fun areItemsTheSame(oldItem: NewsFeed, newItem: NewsFeed): Boolean {
            return oldItem.feedUrl == newItem.feedUrl
        }

        override fun areContentsTheSame(oldItem: NewsFeed, newItem: NewsFeed): Boolean {
            return oldItem == newItem
        }
    }
}