package com.example.carexplorer.presenter

import com.example.carexplorer.R
import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.data.model.enities.Article
import com.example.carexplorer.repository.cache.ContentCache
import com.example.carexplorer.view.ListArticlesView
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@AutoFactory
@InjectViewState
class ListArticlesPresenter @Inject constructor(
    @Provided private val cache : ContentCache
) : MvpPresenter<ListArticlesView>() {
    private val articlesCache = cache
    private val presenterJob = Job()
    fun getArticles(list: List<Article>) {
        CoroutineScope(Dispatchers.Main + presenterJob).launch {
            viewState.showLoading()
            viewState.showListArticles(convertEntries(list)!!)
            viewState.hideLoading()
        }
    }


    fun saveEntry(cachedArticle : CachedArticle) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                cachedArticle.cached = true
                articlesCache.saveArticle(cachedArticle)
                viewState.showMessage(R.string.succesfull)
            }
        } catch (e : Exception) {
            e.printStackTrace()
            viewState.showMessage(R.string.errorSavingToDb)
        }


    }

    fun removeEntry(cachedArticle: CachedArticle) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                cachedArticle.cached = false
                articlesCache.removeArticleByTitle(cachedArticle.title)
                viewState.showMessage(R.string.deleted)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            viewState.showMessage(R.string.errorDeleteFromDb)
        }
    }

    private suspend fun convertEntries(list: List<Article>): List<CachedArticle>? {
        val listEntries: MutableList<CachedArticle> = mutableListOf()
        list.forEach {
            listEntries.add(
                CachedArticle(
                    title = it.title,
                    image = it.image,
                    content = it.content,
                    type = "entry",
                    cached = checkCachedEntries(it.title)
                )
            )
        }
        return listEntries
    }

    private suspend fun checkCachedEntries(newsTitle : String) : Boolean {
        var isCached = false
        withContext(Dispatchers.IO) {
            if (articlesCache.getArticleByTitle(newsTitle) != null) {
                isCached = true
            }
        }
        return isCached
    }
    fun stopWork() {
        presenterJob.cancel()
    }
}