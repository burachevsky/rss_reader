package com.burachevsky.rssfeedreader.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.burachevsky.rssfeedreader.R
import com.burachevsky.rssfeedreader.ui.adapters.FeedsListAdapter
import com.burachevsky.rssfeedreader.databinding.DialogFilterNewsBinding
import com.burachevsky.rssfeedreader.ui.screens.newslist.All
import com.burachevsky.rssfeedreader.ui.screens.newslist.Favorites
import com.burachevsky.rssfeedreader.ui.screens.newslist.NewsListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FilterFeedsDialogFragment(
    private val viewModel: NewsListViewModel
) : DialogFragment() {

    private lateinit var job: Job

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val binding = DataBindingUtil.inflate<DialogFilterNewsBinding>(
            inflater, R.layout.dialog_filter_news, null, false
        )

        val dialog = AlertDialog.Builder(activity)
            .setView(binding.root)
            .setTitle(R.string.filtering_news)
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
            .create()

        binding.apply {
            all.setOnClickListener {
                viewModel.filterList(All)
                dialog.cancel()
            }

            favorites.setOnClickListener {
                viewModel.filterList(Favorites)
                dialog.cancel()
            }

            recyclerView.adapter = FeedsListAdapter(viewModel, dialog::cancel).apply {
                job = CoroutineScope(Dispatchers.Main).launch {
                    viewModel.state.collect {
                        submitList(it.feeds)
                    }
                }
            }
        }

        return dialog
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}
