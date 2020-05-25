package com.example.carexplorer.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.example.carexplorer.R
import com.example.carexplorer.di.App
import com.example.carexplorer.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_webpage.*

class WebPageFragment : BaseFragment() {
    override val layoutId: Int = R.layout.fragment_webpage
    override var titleToolbar = ""
    override val showToolbar: Boolean
        get() = true

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        }
        else {
            super.onBackPressed()
        }

    }

    companion object {
        val tag = "webPageFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

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

        base {
            val args = intent.getBundleExtra("args")
            val page = args?.getString("page")
            val title = args?.getString("title")
            titleToolbar = title!!

            setupWebView(page!!)
        }


    }

    override fun onResume() {
        super.onResume()
        base {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }
    }



    @SuppressLint("SetJavaScriptEnabled")
    fun setupWebView(url : String) {
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        val wvSettings = webView.settings
        wvSettings.apply {
            cacheMode = WebSettings.LOAD_DEFAULT
            setAppCacheEnabled(true)
            javaScriptEnabled = true
            //displayZoomControls = true
            //loadsImagesAutomatically = true
            useWideViewPort = true
            loadWithOverviewMode = true
                javaScriptCanOpenWindowsAutomatically = true
            mediaPlaybackRequiresUserGesture = false
            domStorageEnabled = true
            setSupportMultipleWindows(true)
            //loadWithOverviewMode = true
            //allowContentAccess = true
            //allowUniversalAccessFromFileURLs = true

        }
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.webViewClient = object : WebViewClient() {


//            override fun shouldOverrideUrlLoading(
//                view: WebView?,
//                request: WebResourceRequest?
//            ): Boolean {
//                //cpvWeb.visibility = View.VISIBLE
//                view?.loadUrl(url)
//                return true
//            }
//
//            @RequiresApi(Build.VERSION_CODES.M)
//            override fun onReceivedError(
//                view: WebView?,
//                request: WebResourceRequest?,
//                error: WebResourceError?
//            ) {
//                Snackbar.make(
//                    webView,
//                    "Ошибка!: $error",
//                    Snackbar.LENGTH_LONG
//                )
//            }

//            override fun onPageFinished(view: WebView?, url: String?) {
//                cpvWeb.visibility = View.GONE
//                super.onPageFinished(view, url)
//            }
        }
        webView.isHorizontalScrollBarEnabled = false
        webView.fitsSystemWindows = true
        webView.loadUrl(url)
    }
}