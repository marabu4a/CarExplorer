package com.example.carexplorer.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.example.carexplorer.R
import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.helpers.navigation.parentRouter
import com.example.carexplorer.presenter.ArticlePresenter
import com.example.carexplorer.presenter.ArticlePresenterFactory
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.util.ParcelableArgsBundler
import com.example.carexplorer.view.ArticleView
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import kotlinx.android.synthetic.main.fragment_article.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject


@FragmentWithArgs
class ArticleFragment : BaseFragment(), ArticleView {
    override val layoutRes: Int = R.layout.fragment_article

    @Arg(bundler = ParcelableArgsBundler::class)
    lateinit var article: CachedArticle


    @Inject
    lateinit var presenterFactory: ArticlePresenterFactory
    private val presenter: ArticlePresenter by moxyPresenter {
        presenterFactory.create(parentRouter)
    }

    override val isBottomBarVisible: Boolean
        get() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.loadArticle(article.image!!, article.content!!)


    }

    companion object {
        val tag = "articleFragment"
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun showArticle(image: String, text: String) {
        //val imageGetter = GlideImageGetter(requireActivity(),tvTextArticle, Glide.with(this))
//        val styledText = HtmlCompat.fromHtml(text,HtmlCompat.FROM_HTML_MODE_COMPACT,imageGetter,null)
//        tvTextArticle.also {
//            it.text = styledText
//            it.movementMethod = LinkMovementMethod.getInstance()
//        }
        //TODO try to set custom font
        articleWebText.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadsImagesAutomatically = true
            loadWithOverviewMode = true
        }
        articleWebText.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        articleWebText.fitsSystemWindows = true

        val head =
            "<head><style>@font-face {font-family: 'myFont';src: url('file:///assets/fonts/futura_pt_demi.ttf');}body {font-family: 'myFont';}</style></head>"

        val finalLink = "<html>$head<body style=\"font-family: myFont\">$text</body></html>"
        articleWebText.loadDataWithBaseURL(
            "",
            finalLink,
            "text/html",
            "utf-8",
            null
        )
        //Picasso.get().load(image).into(ivImageArticle)
    }

    override fun showLoading() {
        //tvTextArticle.visibility = View.GONE
        //ivImageArticle.visibility = View.GONE
    }

    override fun hideLoading() {
        //tvTextArticle.visibility = View.VISIBLE
        //ivImageArticle.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }
}