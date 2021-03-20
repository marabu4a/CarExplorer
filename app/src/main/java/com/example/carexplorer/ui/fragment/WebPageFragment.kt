package com.example.carexplorer.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
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
    lateinit var page: String

    @Arg
    lateinit var title: String

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
        setupWebView()
        webPageToolbar.title = title
        presenter.loadUrl(page)
    }

    override fun loadUrl(url: String) {
        webPageLoadingIndicator.isVisible = true
        webView.loadUrl(url)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                webPageLoadingIndicator.isVisible = true
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                webPageLoadingIndicator.isVisible = false
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
            useWideViewPort = true

        }
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.fitsSystemWindows = true

    }

    companion object {
        val tag = "webPageFragment"
    }
}