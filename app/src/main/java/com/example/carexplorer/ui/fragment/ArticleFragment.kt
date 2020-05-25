package com.example.carexplorer.ui.fragment
import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.carexplorer.R
import com.example.carexplorer.di.App
import com.example.carexplorer.presenter.ArticlePresenter
import com.example.carexplorer.repository.remote.ApiService
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.view.ArticleView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment : BaseFragment(),ArticleView {
    override val layoutId: Int = R.layout.fragment_article

    override val showToolbar: Boolean = false

    override var titleToolbar: String = "Новости"


    @InjectPresenter
    lateinit var presenter : ArticlePresenter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        base {
            val args = intent.getBundleExtra("args")
            args?.let {
                val image = it.getString(ApiService.PARAM_IMAGE_ARTICLE)
                val text = it.getString(ApiService.PARAM_TEXT_ARTICLE)
                val title = it.getString(ApiService.PARAM_TITLE_ARTICLE)

                titleToolbar = title!!
                presenter.loadArticle(image!!,text!!)
            }

        }

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

    override fun showArticle(image : String,text : String) {
        //val imageGetter = GlideImageGetter(requireActivity(),tvTextArticle, Glide.with(this))
//        val styledText = HtmlCompat.fromHtml(text,HtmlCompat.FROM_HTML_MODE_COMPACT,imageGetter,null)
//        tvTextArticle.also {
//            it.text = styledText
//            it.movementMethod = LinkMovementMethod.getInstance()
//        }

        webText.settings.loadWithOverviewMode = true
        webText.settings.javaScriptEnabled = true
        webText.isHorizontalScrollBarEnabled = false
        webText.loadDataWithBaseURL(null, "<style>@font-face {font-family: 'arial';src: url('file:///assets/fonts/FuturaPT_Medium.woff');}body " +
                "{font-family: 'verdana';}" + "</style>\n"+ "<style>img{display: inline; height: auto; max-width: 100%;} " +
                "</style>\n" + "<style>iframe{ height: auto; width: auto;}" + "</style>\n" + text, null, "utf-8", null)

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