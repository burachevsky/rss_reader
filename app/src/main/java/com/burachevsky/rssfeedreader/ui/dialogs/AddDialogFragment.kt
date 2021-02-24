package com.burachevsky.rssfeedreader.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.burachevsky.rssfeedreader.R
import com.burachevsky.rssfeedreader.databinding.DialogAddFeedBinding
import com.burachevsky.rssfeedreader.ui.screens.newslist.DownloadFeed
import com.burachevsky.rssfeedreader.ui.screens.newslist.NewsListViewModel

class AddDialogFragment(
    private val viewModel: NewsListViewModel
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val inflater = requireActivity().layoutInflater
            val binding = DataBindingUtil.inflate<DialogAddFeedBinding>(
                inflater, R.layout.dialog_add_feed, null, false
            )

            val dialog = AlertDialog.Builder(it)
                .setView(binding.root)
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(R.string.add_feed) { _, _ ->
                    val url = binding.inputText.text.toString()
                    viewModel.submit(DownloadFeed(url))
                }
                .create()

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}