package com.one.word.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.os.bundleOf
import com.one.adapter.MultiAdapter
import com.one.coreapp.ui.adapters.SpaceAdapter
import com.one.coreapp.ui.base.fragments.BaseViewModelSheetFragment
import com.one.coreapp.utils.Utils
import com.one.coreapp.utils.autoCleared
import com.one.coreapp.utils.extentions.setDebouncedClickListener
import com.one.coreapp.utils.extentions.setVisible
import com.one.navigation.NavigationEvent
import com.one.word.R
import com.one.word.databinding.FragmentWordDetailBinding
import com.one.word.ui.adapters.SpellingAdapter
import com.one.word.ui.adapters.TextAdapter
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import java.util.*

class WordDetailFragment : BaseViewModelSheetFragment<FragmentWordDetailBinding, WordDetailViewModel>(R.layout.fragment_word_detail) {

    private val text: String by lazy {
        requireArguments().getString(TEXT)!!
    }

    private val inputCode: String by lazy {
        requireArguments().getString(INPUT_CODE)!!
    }

    private var multiAdapter by autoCleared<MultiAdapter>()


    override fun getParameter(): ParametersDefinition {
        return { parametersOf(text, inputCode) }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCall()
        setupCopy()
        setupShare()
        setupExport()
        setupToolbar()
        setupWebView()
        setupRecyclerView()

        observeData()
    }

    override fun onResume() {
        super.onResume()
        binding?.webView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding?.webView?.onPause()
    }

    override fun onDestroyView() {
        binding?.webView?.destroy()
        super.onDestroyView()
    }

    private fun setupCall() {

        val binding = binding ?: return

        binding.ivCopy.setDebouncedClickListener {

            Utils.call(requireActivity(), text)
        }
    }

    private fun setupCopy() {

        val binding = binding ?: return

        binding.ivCopy.setOnClickListener {

            Utils.copyText(requireContext(), text)
        }
    }

    private fun setupShare() {

        val binding = binding ?: return

        binding.ivShare.setOnClickListener {

            Utils.shareText(requireActivity(), text)
        }
    }

    private fun setupExport() {

        val binding = binding ?: return

        binding.ivExport.setOnClickListener {

            Utils.export(requireActivity(), text)
        }
    }

    private fun setupToolbar() {

        val binding = binding ?: return

        binding.ivClose.setOnClickListener {
            dismiss()
        }

        binding.tvTitle.setText(text)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {

        val binding = binding ?: return

        binding.frameSearch.setDebouncedClickListener {

            viewModel.updateWebVisible(true)
        }

        binding.webView.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView, newProgress: Int) {

                binding.progress.progress = newProgress
                binding.progress.setVisible(newProgress != 100)
            }
        }

        binding.webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String?) {

            }
        }

        binding.webView.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY

        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.databaseEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.loadWithOverviewMode = true
    }

    private fun setupRecyclerView() {

        val binding = binding ?: return

        val textAdapter = TextAdapter()

        val spellingAdapter = SpellingAdapter()

        multiAdapter = MultiAdapter(SpaceAdapter(), textAdapter, spellingAdapter).apply {
            setRecyclerView(binding.recyclerView)
        }
    }

    private fun observeData() = with(viewModel) {

        isLink.observe(viewLifecycleOwner) {

            val binding = binding ?: return@observe

            binding.ivExport.setVisible(it)
        }

        webVisible.observe(viewLifecycleOwner) {

            val binding = binding ?: return@observe

            binding.webView.setVisible(it)
            binding.frameSearch.setVisible(!it)
        }

        webLoadData.observe(viewLifecycleOwner) {

            val binding = binding ?: return@observe

            binding.webView.loadUrl(it)
        }

        isNumberPhone.observe(viewLifecycleOwner) {

            val binding = binding ?: return@observe

            binding.ivCall.setVisible(it)
        }

        viewItemListDisplay.observe(viewLifecycleOwner) {

            multiAdapter?.submitList(it)
        }

        viewModel.updateTargetLanguage(Locale.getDefault().language)
    }

    companion object {

        private const val TEXT = "TEXT"
        private const val INPUT_CODE = "INPUT_CODE"

        fun newInstance(text: String, inputCode: String = "") = WordDetailFragment().apply {

            arguments = bundleOf(TEXT to text, INPUT_CODE to inputCode)
        }
    }
}

data class WordDetailNavigationEvent(val text: String, val inputCode: String) : NavigationEvent()