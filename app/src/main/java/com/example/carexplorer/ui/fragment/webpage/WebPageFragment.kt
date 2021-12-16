package com.example.carexplorer.ui.fragment.webpage

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.core.view.isVisible
import com.example.carexplorer.R
import com.example.carexplorer.helpers.loadImageUrl
import com.example.carexplorer.helpers.navigation.parentRouter
import com.example.carexplorer.presenter.WebPagePresenter
import com.example.carexplorer.presenter.WebPagePresenterFactory
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.view.WebPageView
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import kotlinx.android.synthetic.main.fragment_webpage.*
import moxy.ktx.moxyPresenter
import org.adblockplus.libadblockplus.android.settings.AdblockHelper
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@FragmentWithArgs
class WebPageFragment : BaseFragment(), WebPageView {
    override val layoutRes: Int = R.layout.fragment_webpage

    @Inject
    lateinit var presenterFactory: WebPagePresenterFactory

    private val presenter: WebPagePresenter by moxyPresenter {
        presenterFactory.create(parentRouter)
    }

    private var javaScript: String? = null
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
        webViewSwipeRefresh.setOnRefreshListener {
            webView.reload()
        }
        setupWebView()
        presenter.loadUrl(webPageBundle.page)
    }

    override fun onDestroyView() {
        webView?.dispose(null)
        super.onDestroyView()
    }

    override fun loadUrl(url: String) {
        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            presenter.onBackPressed()
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {

        javaScript = presenter.readJavaScript(resources.openRawResource(R.raw.media_functions))

        webView.setProvider(AdblockHelper.get().provider)
        webView.webViewClient = object : WebViewClient() {

        }
        webView.settings.apply {
            cacheMode = WebSettings.LOAD_NO_CACHE
            javaScriptEnabled = true
            domStorageEnabled = true
            useWideViewPort = true
            loadsImagesAutomatically = true
            loadWithOverviewMode = true
            allowFileAccess = true
            allowContentAccess = true
            mediaPlaybackRequiresUserGesture = false
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            loadWithOverviewMode = true
            cacheMode = WebSettings.LOAD_DEFAULT
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            databaseEnabled = true
            offscreenPreRaster = true
            javaScriptCanOpenWindowsAutomatically = true
            mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        }
        webView.apply {
            scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            setRendererPriorityPolicy(WebView.RENDERER_PRIORITY_IMPORTANT, false)
            fitsSystemWindows = true
            mediaFunctions()
        }
        initWebChromeClient()
        initWebViewClient()
    }

    private fun initWebChromeClient() {
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    webPageLoading?.isVisible = false
                    webViewSwipeRefresh?.isRefreshing = false
                    mediaFunctions()
                }
            }

            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                webPageToolbar.title = title
                mediaFunctions()
            }

            override fun onShowCustomView(view: View, callback: CustomViewCallback) {}

            override fun onHideCustomView() {}

            override fun onPermissionRequest(request: PermissionRequest) {
                val resources = request.resources
                for (resource in resources) {
                    if (PermissionRequest.RESOURCE_PROTECTED_MEDIA_ID == resource) {
                        request.grant(arrayOf(PermissionRequest.RESOURCE_PROTECTED_MEDIA_ID))
                        return
                    }
                }
                super.onPermissionRequest(request)
            }
        }
    }

    private fun initWebViewClient() {
        webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                return super.shouldInterceptRequest(view, request)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
                if (url == null) {
                    return super.shouldInterceptRequest(view, url as String)

                }

                val interceptedUrl = url.lowercase(Locale.ROOT)
                val cacheableImage = CacheableImage.getMatchingImage(interceptedUrl)

                return when {
                    cacheableImage != null -> {
                        try {
                            val bitmap = webView.loadImageUrl(interceptedUrl)

                            WebResourceResponse(
                                cacheableImage.mimeType,
                                "UTF-8",
                                presenter.getBitmapInputStream(
                                    bitmap,
                                    cacheableImage.compressFormat
                                )
                            )
                        } catch (e: Exception) {
                            Timber.e("Error while loading images: $e")
                            super.shouldInterceptRequest(view, interceptedUrl)
                        }

                    }
                    else -> super.shouldInterceptRequest(view, interceptedUrl)
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                webPageLoading?.isVisible = false
                webViewSwipeRefresh?.isRefreshing = false
                super.onPageFinished(view, url)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                webPageLoading?.isVisible = true
                super.onPageStarted(view, url, favicon)
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
            }
        }
    }

    private fun mediaFunctions() {
        mediaResetEventListener()
        loadJavaScript(javaScript)
        mediaSetEventListener()
    }

    private fun buildDesktopUserAgentString(agent: String): String =
        agent
            .replace("Android", "X11;")
            .replace("Mobile", "")
            .replace("wv", "")
            .replace("Version/4.0", "")

    private fun mediaResetEventListener() = loadJavaScript("mediaResetEventListener();")

    private fun mediaSetEventListener() = loadJavaScript("mediaSetEventListener();")

    private fun loadJavaScript(javaScript: String?) {
        if (javaScript != null) webView?.loadUrl("javascript:$javaScript")
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