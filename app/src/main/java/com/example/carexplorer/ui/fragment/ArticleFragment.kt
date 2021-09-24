package com.example.carexplorer.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.example.carexplorer.R
import com.example.carexplorer.data.model.enities.Article
import com.example.carexplorer.helpers.navigation.parentRouter
import com.example.carexplorer.helpers.setImageUrl
import com.example.carexplorer.helpers.util.HTMLUtil
import com.example.carexplorer.helpers.util.ParcelableArgsBundler
import com.example.carexplorer.presenter.ArticlePresenter
import com.example.carexplorer.presenter.ArticlePresenterFactory
import com.example.carexplorer.ui.base.BaseFragment
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
    lateinit var article: Article


    @Inject
    lateinit var presenterFactory: ArticlePresenterFactory
    private val presenter: ArticlePresenter by moxyPresenter {
        presenterFactory.create(parentRouter)
    }

    override val isBottomBarVisible: Boolean
        get() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        articlePreviewImage.setImageUrl(
            article.image,
            onBitmapCallback = {
                startPostponedEnterTransition()
            },
            onError = {
                startPostponedEnterTransition()
            }
        )
        with(articleToolbar) {
            title = article.title
        }
        presenter.loadArticle(article.image, article.content)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun showArticle(image: String, text: String) {
        articleWebText.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadsImagesAutomatically = true
            loadWithOverviewMode = true
            minimumFontSize = 14
        }
        articleWebText.fitsSystemWindows = true

        articleWebText.loadDataWithBaseURL(
            "file:///android_asset/",
            HTMLUtil.getHtmlWithNewFont(text),
            "text/html",
            "utf-8",
            null
        )
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

    companion object {
        val tag = "articleFragment"
    }
}