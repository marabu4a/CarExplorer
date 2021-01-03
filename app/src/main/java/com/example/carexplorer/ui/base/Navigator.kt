package com.example.carexplorer.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.data.model.Category
import com.example.carexplorer.data.model.Source
import com.example.carexplorer.repository.remote.ApiService
import com.example.carexplorer.ui.activity.*
import com.google.gson.Gson
import javax.inject.Inject


class Navigator @Inject constructor(

) {

    fun showListArticles(context: Context,category : Category) {
        val bundle = Bundle()
        val categories = Gson().toJson(category.entries)
        bundle.putString(ApiService.PARAM_NAME_CATEGORY,category.name)
        bundle.putString(ApiService.PARAM_CATEGORIES,categories)
        context.startActivity<ListArticlesActivity>(args = bundle)
    }

    fun showFavorites(context: Context) = context.startActivity<FavoritesActivity>()

    fun showHome(context: Context) = context.startActivity<AppActivity>()

    fun showArticle(context: Context, cachedArticle: CachedArticle) {
        val bundle = Bundle()
        bundle.putString(ApiService.PARAM_IMAGE_ARTICLE,cachedArticle.image)
        bundle.putString(ApiService.PARAM_TEXT_ARTICLE,cachedArticle.content)
        bundle.putString(ApiService.PARAM_TITLE_ARTICLE,cachedArticle.title)
        context.startActivity<ArticleActivity>(args = bundle)
    }

    fun showNews(context: Context,source : Source) {
        val bundle = Bundle()
        bundle.putString(ApiService.PARAM_LINK_NEWS,source.url)
        bundle.putString(ApiService.PARAM_TITLE_ARTICLE,source.title)
        context.startActivity<NewsActivity>(args = bundle)
    }

    fun showWebPage(context: Context,webPage : String?,titleWebPage: String?) {
        val bundle = Bundle()
        bundle.putString("page",webPage)
        bundle.putString("title",titleWebPage)
        context.startActivity<WebPageActivity>(args = bundle)
    }

}

private inline fun <reified T> Context.startActivity(newTask: Boolean = false, args: Bundle? = null) {
    this.startActivity(Intent(this, T::class.java).apply {
        if (newTask) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        putExtra("args", args)
    })
}



