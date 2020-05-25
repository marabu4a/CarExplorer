package com.example.carexplorer.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.carexplorer.R
import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.repository.cache.ContentCache
import com.example.carexplorer.view.FavoritesView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@InjectViewState
class FavoritesPresenter @Inject constructor(
    cache : ContentCache
): MvpPresenter<FavoritesView>() {
    private val articlesCache = cache
    private lateinit var  items : List<CachedArticle>

    fun fetchCachedArticles() {
        try {
            CoroutineScope(Dispatchers.Main).launch {
                viewState.startLoading()
                withContext(Dispatchers.IO) {
                    items = articlesCache.getArticles()
                }
                items = items.reversed()
                viewState.showContent(items)
                viewState.endLoading()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            viewState.showMessage(R.string.errorLoadingFromDb)
        }
    }

    fun deleteCachedArticles(article : CachedArticle) {
        try {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    articlesCache.removeArticleByTitle(article.title)
                }

            }
            viewState.showMessage(R.string.deleteCachedFromFavorites)
        }
        catch(e:Exception) {
            e.printStackTrace()
            viewState.showMessage(R.string.errorDeleteFromDb)
        }

    }


    fun filterList(tag : Int,list : List<CachedArticle>) {
        var filterItems: java.util.ArrayList<CachedArticle>
        when (tag) {
            0 -> {
                filterItems = list.filter { it.type == "news" } as ArrayList<CachedArticle>
                viewState.showContent(filterItems)
            }
            1 -> {
               filterItems = list.filter { it.type == "entry" } as ArrayList<CachedArticle>
                viewState.showContent(filterItems)
            }
            else -> {
                filterItems = list as ArrayList<CachedArticle>
                viewState.showContent(filterItems)
            }
        }
    }

}