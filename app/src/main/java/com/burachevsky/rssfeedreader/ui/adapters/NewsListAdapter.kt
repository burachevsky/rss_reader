package com.burachevsky.rssfeedreader.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.burachevsky.rssfeedreader.R
import com.burachevsky.rssfeedreader.databinding.ListItemNewsBinding
import com.burachevsky.rssfeedreader.data.domainobjects.NewsItem
import com.burachevsky.rssfeedreader.ui.screens.newslist.NewsListViewModel

class NewsListAdapter (
    val newsListViewModel: NewsListViewModel
): ListAdapter<NewsItem, NewsListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_news,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        internal val binding: ListItemNewsBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
           binding.apply {
               viewModel = newsListViewModel
               root.setOnClickListener {
                   newsListViewModel.showItemDetails(binding.item)
               }
               likeButton.onLikeListener = { value ->
                   newsListViewModel.setLiked(item, value)
               }
               itemMenu.setOnClickListener {
                   newsListViewModel.showItemMenu(itemMenu, item)
               }
           }
        }

        fun bind(newsItem: NewsItem) {
            binding.apply {
                item = newsItem
                likeButton.init(newsItem.isInCollection)
                executePendingBindings()
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<NewsItem>() {
        override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem.identifier == newItem.identifier
        }

        override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem == newItem
        }
    }
}
