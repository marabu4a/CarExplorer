package com.example.carexplorer.presenter

import com.example.carexplorer.R
import com.example.carexplorer.data.cache.ArticlesCache
import com.example.carexplorer.data.cache.NewsCache
import com.example.carexplorer.data.model.enities.Article
import com.example.carexplorer.data.model.enities.Favorite
import com.example.carexplorer.data.model.enities.News
import com.example.carexplorer.ui.base.ErrorHandler
import com.example.carexplorer.view.FavoritesView
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import javax.inject.Inject

@AutoFactory
@InjectViewState
class FavoritesPresenter @Inject constructor(
    @Provided private val articleCache: ArticlesCache,
    @Provided private val newsCache: NewsCache,
    @Provided override val errorHandler: ErrorHandler
): BasePresenter<FavoritesView>(errorHandler) {
    private var favorites: ArrayList<Favorite>? = null

    fun fetchCachedEntries() {
        //if (favorites != null) return
        try {
            viewState.showLoading()
            CoroutineScope(Dispatchers.IO).launch {
                articleCache.getArticles().zip(newsCache.getNews()) { one, two ->
                    one + two
                }.collectLatest {
                    withContext(Dispatchers.Main) {
                        viewState.hideLoading()
                        favorites = ArrayList(it)
                        viewState.showContent(favorites ?: arrayListOf())
                    }
                }
            }
            //CoroutineScope(Dispatchers.Main).launch {
            //    viewState.showLoading()
            //    withContext(Dispatchers.IO) {
            //        favorites?.addAll(articleCache.getArticles())
            //        favorites?.addAll(newsCache.getNews())
            //    }
            //    favorites = favorites
            //    viewState.showContent(favorites ?: arrayListOf())
            //    viewState.hideLoading()
            //}
        }
        catch (e: Exception) {
            e.printStackTrace()
            viewState.showMessage(R.string.errorLoadingFromDb)
        }
    }

    fun deleteCachedEntry(entry: Favorite) {
        try {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    if (entry is News) {
                        newsCache.removeNewsByTitle(entry.title)
                    } else if (entry is Article) {
                        articleCache.removeArticleByTitle(entry.title)
                    }
                }
            }
            favorites?.remove(entry)
            viewState.showContent(favorites ?: arrayListOf())
            viewState.showMessage(R.string.deleteCachedFromFavorites)
        }
        catch(e:Exception) {
            e.printStackTrace()
            viewState.showMessage(R.string.errorDeleteFromDb)
        }

    }

    //fun filterFavorites() {
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