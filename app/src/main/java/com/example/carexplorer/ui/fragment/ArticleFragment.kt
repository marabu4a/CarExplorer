package com.example.carexplorer.ui.fragment

import android.os.Bundle
import android.view.View
import com.example.carexplorer.R
import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.presenter.ArticlePresenter
import com.example.carexplorer.presenter.ArticlePresenterFactory
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.util.ParcelableArgsBundler
import com.example.carexplorer.view.ArticleView
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_article.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject


@FragmentWithArgs
class ArticleFragment : BaseFragment(), ArticleView {
    override val layoutRes: Int = R.layout.fragment_article

    @Inject
    lateinit var presenterFactory: ArticlePresenterFactory

    @Arg(bundler = ParcelableArgsBundler::class)
    lateinit var article: CachedArticle

    private val presenter: ArticlePresenter by moxyPresenter {
        presenterFactory.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.loadArticle(article.image!!, article.url!!)


    }

    override fun onResume() {
        super.onResume()
        base {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
        }
    }

    companion object {
        val tag = "articleFragment"
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.stopWork()
    }

    override fun showArticle(image: String, text: String) {
        //val imageGetter = GlideImageGetter(requireActivity(),tvTextArticle, Glide.with(this))
//        val styledText = HtmlCompat.fromHtml(text,HtmlCompat.FROM_HTML_MODE_COMPACT,imageGetter,null)
//        tvTextArticle.also {
//            it.text = styledText
//            it.movementMethod = LinkMovementMethod.getInstance()
//        }

        webText.settings.loadWithOverviewMode = true
        webText.settings.javaScriptEnabled = true
        webText.isHorizontalScrollBarEnabled = false
        webText.loadDataWithBaseURL(
            null,
            "<style>@font-face {font-family: 'arial';src: url('file:///assets/fonts/FuturaPT_Medium.woff');}body " +
                    "{font-family: 'verdana';}" + "</style>\n" + "<style>img{display: inline; height: auto; max-width: 100%;} " +
                    "</style>\n" + "<style>iframe{ height: auto; width: auto;}" + "</style>\n" + text,
            null,
            "utf-8",
            null
        )

        Picasso.get().load(image).into(ivImageArticle)
    }

    override fun startLoading() {
        //tvTextArticle.visibility = View.GONE
        ivImageArticle.visibility = View.GONE

    }

    override fun endLoading() {
        //tvTextArticle.visibility = View.VISIBLE
        ivImageArticle.visibility = View.VISIBLE

    }
}