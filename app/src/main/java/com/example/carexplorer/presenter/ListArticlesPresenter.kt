package com.example.carexplorer.presenter

import com.example.carexplorer.R
import com.example.carexplorer.data.model.enities.Article
import com.example.carexplorer.data.model.retrofit.usecase.articles.GetArticlesByCategoryUseCase
import com.example.carexplorer.repository.cache.ArticlesCache
import com.example.carexplorer.ui.base.ErrorHandler
import com.example.carexplorer.view.ListArticlesView
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import kotlinx.coroutines.*
import moxy.InjectViewState
import javax.inject.Inject

@AutoFactory
@InjectViewState
class ListArticlesPresenter @Inject constructor(
    @Provided private val articlesCache : ArticlesCache,
    @Provided private val getArticlesByCategoryUseCase: GetArticlesByCategoryUseCase,
    @Provided override val errorHandler: ErrorHandler
) : BasePresenter<ListArticlesView>(errorHandler) {
    private val presenterJob = Job()

    fun fetchArticles(category: String) {
        getArticlesByCategoryUseCase.onObtain { params, deferred ->
            viewState.showLoading()
            val result = deferred.await().apply {
                viewState.hideLoading()
                viewState.showListArticles(this.map { it.copy(isFavorite = checkCachedArticles(it.title)) })
            }
        }.execute(params = category)
    }


    fun saveArticle(article: Article) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                articlesCache.saveArticle(article.copy(isFavorite = true))
                viewState.showMessage(R.string.succesfull)
            }
        } catch (e : Exception) {
            e.printStackTrace()
            viewState.showMessage(R.string.errorSavingToDb)
        }


    }

    fun removeArticle(article: Article) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                articlesCache.removeArticleByTitle(article.title)
                viewState.showMessage(R.string.deleted)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            viewState.showMessage(R.string.errorDeleteFromDb)
        }
    }

    //private suspend fun convertEntries(list: List<Article>): List<CachedArticle>? {
    //    val listEntries: MutableList<CachedArticle> = mutableListOf()
    //    list.forEach {
    //        listEntries.add(
    //            CachedArticle(
    //                title = it.title,
    //                image = it.image,
    //                content = it.content,
    //                type = "entry",
    //                cached = checkCachedEntries(it.title)
    //            )
    //        )
    //    }
    //    return listEntries
    //}
    //
    private suspend fun checkCachedArticles(articleTitle : String) : Boolean {
        var isCached = false
        withContext(Dispatchers.IO) {
            if (articlesCache.getArticleByTitle(articleTitle) != null) {
                isCached = true
            }
        }
        return isCached
    }


    fun stopWork() {
        presenterJob.cancel()
    }
}