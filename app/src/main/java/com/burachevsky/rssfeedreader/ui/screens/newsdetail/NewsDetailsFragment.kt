package com.burachevsky.rssfeedreader.ui.screens.newsdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.burachevsky.rssfeedreader.R
import com.burachevsky.rssfeedreader.databinding.FragmentNewsDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.list_item_news.*
import javax.inject.Inject

@AndroidEntryPoint
class NewsDetailsFragment : Fragment() {

    private val args: NewsDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: NewsDetailsViewModelFactory

    private val viewModel: NewsDetailsViewModel by viewModels {
        NewsDetailsViewModel.provideFactory(
            viewModelFactory, args.item
        )
    }

    private lateinit var binding: FragmentNewsDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_news_details, container, false
        )

        binding.apply {
            viewModel = this@NewsDetailsFragment.viewModel
            lifecycleOwner = this@NewsDetailsFragment
            htmlViewer.linksClickable = true
            htmlViewer.movementMethod = LinkMovementMethod.getInstance()
        }

        viewModel.item.observe(viewLifecycleOwner) { item ->
            binding.likeButton.init(item.isInCollection)
            likeButton.onLikeListener = { value ->
                viewModel.likeItem(item, value)
            }
            viewModel.readItem(item)
        }

        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            binding.scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

                binding.toolbarCard.visibility =
                    if (scrollY <= oldScrollY)
                        View.VISIBLE
                    else View.GONE
            }
        }*/

        setupButtons()

        return binding.root
    }

    private fun setupButtons() {
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.openInBrowser.setOnClickListener {
            val newsItem = viewModel.item.value!!
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsItem.itemLink))
            startActivity(intent)
        }

        binding.share.setOnClickListener {
            val newsItem = viewModel.item.value!!
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, newsItem.itemLink)
                type = "text/plain"
            }
            startActivity(intent)
        }
    }
}
