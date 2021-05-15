package com.example.carexplorer.presenter

import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.repository.cache.ArticlesCache
import com.example.carexplorer.view.FavoritesView
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@AutoFactory
@InjectViewState
class FavoritesPresenter @Inject constructor(
    @Provided private val cachedDb: ArticlesCache
): MvpPresenter<FavoritesView>() {
    private var favoriteArticles: List<CachedArticle>? = null

    //fun fetchCachedArticles() {
    //    if (favoriteArticles != null) return
    //    try {
    //        CoroutineScope(Dispatchers.Main).launch {
    //            viewState.showLoading()
    //            withContext(Dispatchers.IO) {
    //                favoriteArticles = cachedDb.getArticles()
    //            }
    //            favoriteArticles = favoriteArticles?.reversed()
    //            viewState.showContent(favoriteArticles.orEmpty())
    //            viewState.hideLoading()
    //        }
    //    }
    //    catch (e: Exception) {
    //        e.printStackTrace()
    //        viewState.showMessage(R.string.errorLoadingFromDb)
    //    }
    //}
    //
    //fun deleteCachedArticles(article : CachedArticle) {
    //    try {
    //        CoroutineScope(Dispatchers.Main).launch {
    //            withContext(Dispatchers.IO) {
    //                cachedDb.removeArticleByTitle(article.title)
    //            }
    //
    //        }
    //        viewState.showMessage(R.string.deleteCachedFromFavorites)
    //    }
    //    catch(e:Exception) {
    //        e.printStackTrace()
    //        viewState.showMessage(R.string.errorDeleteFromDb)
    //    }
    //
    //}
    //
    //
    //fun filterList(tag : Int,list : List<CachedArticle>) {
    //    var filterItems: java.util.ArrayList<CachedArticle>
    //    when (tag) {
    //        0 -> {
    //            filterItems = list.filter { it.type == "news" } as ArrayList<CachedArticle>
    //            viewState.showContent(filterItems)
    //        }
    //        1 -> {
    //           filterItems = list.filter { it.type == "entry" } as ArrayList<CachedArticle>
    //            viewState.showContent(filterItems)
    //        }
    //        else -> {
    //            filterItems = list as ArrayList<CachedArticle>
    //            viewState.showContent(filterItems)
    //        }
    //    }
    //}

}