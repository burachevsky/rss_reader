package com.burachevsky.rssfeedreader.ui.screens.newsdetail

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.*
import androidx.appcompat.view.menu.MenuBuilder
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.burachevsky.rssfeedreader.R
import com.burachevsky.rssfeedreader.databinding.FragmentNewsDetailsBinding
import com.burachevsky.rssfeedreader.ui.base.Renderer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.list_item_news.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewsDetailsFragment : Fragment(),
    Renderer<NewsDetailsState, NewsDetailsEffect> {

    private val args: NewsDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: NewsDetailsViewModelFactory

    private val newsDetailsViewModel: NewsDetailsViewModel by viewModels {
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
        Log.d(TAG, "onCreateView()")

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_news_details, container, false
        )

        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables", "RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated()")
        binding.apply {
            lifecycleOwner = this@NewsDetailsFragment
            htmlViewer.linksClickable = true
            htmlViewer.movementMethod = LinkMovementMethod.getInstance()

            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            shareButton.setOnClickListener {
                newsDetailsViewModel.submit(ShareItem(requireContext()))
            }

            val menu = toolbar.menu
            if (menu is MenuBuilder) {
                menu.setOptionalIconsVisible(true)
            }

            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.like -> {
                        newsDetailsViewModel.submit(
                            if (newsDetailsViewModel.state.value.item.isInCollection)
                                UnlikeItem
                            else LikeItem
                        )
                        true
                    }

                    R.id.openInBrowser -> {
                        newsDetailsViewModel.submit(
                            OpenInBrowser(requireContext())
                        )
                        true
                    }

                    R.id.shareItem -> {
                        newsDetailsViewModel.submit(ShareItem(requireContext()))
                        true
                    }

                    else -> false
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                newsDetailsViewModel.state.collect(::renderState)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun renderState(state: NewsDetailsState) {
        Log.d(TAG, "rendering state")

        binding.apply {
            if (item == null || item!!.itemLink != state.item.itemLink)
                item = state.item
            toolbar.menu.findItem(R.id.like).icon = requireContext().getDrawable(
                if (state.item.isInCollection) R.drawable.ic_like_filled
                else R.drawable.ic_like
            )
            executePendingBindings()
        }
    }

    override fun renderEffect(effect: NewsDetailsEffect) {
        //no effects yet
    }

    override fun onResume() {
        Log.d(TAG, "onResume()")
        super.onResume()
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView()")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        super.onDestroy()
    }

    override fun onPause() {
        Log.d(TAG, "onPause()")
        super.onPause()
    }

    override fun onStart() {
        Log.d(TAG, "onStart()")
        super.onStart()
    }

    override fun onStop() {
        Log.d(TAG, "onStop()")
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate()")
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach()")
        super.onAttach(context)
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach()")
        super.onDetach()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated()")
        super.onActivityCreated(savedInstanceState)
    }


    companion object {
        val TAG = "${NewsDetailsFragment::class.simpleName}"
    }

}
