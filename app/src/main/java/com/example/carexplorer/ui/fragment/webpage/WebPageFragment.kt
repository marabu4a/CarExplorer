package com.example.carexplorer.ui.fragment.webpage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.core.view.isVisible
import com.example.carexplorer.R
import com.example.carexplorer.helpers.navigation.parentRouter
import com.example.carexplorer.presenter.WebPagePresenter
import com.example.carexplorer.presenter.WebPagePresenterFactory
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.view.WebPageView
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import kotlinx.android.synthetic.main.fragment_webpage.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject

@FragmentWithArgs
class WebPageFragment : BaseFragment(), WebPageView {
    override val layoutRes: Int = R.layout.fragment_webpage

    @Inject
    lateinit var presenterFactory: WebPagePresenterFactory

    private val presenter: WebPagePresenter by moxyPresenter {
        presenterFactory.create(parentRouter)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            presenter.onBackPressed()
        }

    }

    override val isBottomBarVisible: Boolean = false

    @Arg
    lateinit var webPageBundle: WebPageBundle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(webPageToolbar) {
            setNavigationOnClickListener { onBackPressed() }
            initShareAndReloadMenu({
                onShareButtonClicked()
            }, {})
        }
        setupWebView()
        presenter.loadUrl(webPageBundle.page)
    }

    override fun loadUrl(url: String) {
        webView.loadUrl(url)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                webPageLoadingIndicator?.isVisible = true
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                webPageLoadingIndicator?.isVisible = false
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {

            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                if (errorResponse?.statusCode == 404) {
                    webPageErrorImage.isVisible = true
                    webPageErrorText.isVisible = true
                }
            }
        }
        webView.settings.apply {
            cacheMode = WebSettings.LOAD_DEFAULT
            setAppCacheEnabled(true)
            javaScriptEnabled = true
            domStorageEnabled = true
            loadsImagesAutomatically = true
            builtInZoomControls = true
            displayZoomControls = false
            loadWithOverviewMode = true
            cacheMode = WebSettings.LOAD_DEFAULT
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            setAppCacheEnabled(true)
            databaseEnabled = true
        }
        webView.fitsSystemWindows = true

    }

    private fun onShareButtonClicked() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            flags = Intent.FLAG_ACTIVITY_NEW_DOCUMENT
            putExtra(Intent.EXTRA_SUBJECT, webPageBundle.title)
            putExtra(Intent.EXTRA_TEXT, webPageBundle.page)
        }
        startActivity(Intent.createChooser(shareIntent, "News URL"))
    }

    companion object {
        val tag = "webPageFragment"
    }
}