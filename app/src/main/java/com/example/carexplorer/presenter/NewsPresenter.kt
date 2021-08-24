package com.example.carexplorer.presenter

import com.example.carexplorer.data.cache.NewsCache
import com.example.carexplorer.data.model.enities.News
import com.example.carexplorer.ui.base.ErrorHandler
import com.example.carexplorer.view.BaseView
import com.prof.rssparser.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
open class NewsPresenter<T : BaseView> @Inject constructor(
    private val newsDb: NewsCache,
    errorHandler: ErrorHandler
) : BasePresenter<T>(errorHandler) {
    private var news = listOf<Article>()

    fun saveArticleToDb(article: News) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                Timber.e("Saving")
                newsDb.saveNews(article.copy(isFavorite = true))
                //viewState.showPositiveMessage(R.string.succesfull)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onError(e)
        }
    }

    fun removeArticleFromDb(article: News) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                newsDb.removeNewsByTitle(article.title)
                //viewState.showMessage(R.string.deleted)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            //viewState.showMessage(R.string.errorDeleteFromDb)
        }

    }

    protected suspend fun checkCachedNews(newsTitle: String): Boolean {
        var isCached = false
        withContext(Dispatchers.IO) {
            if (newsDb.getNewsByTitle(newsTitle) != null) {
                isCached = true
            }
        }
        return isCached
    }
    //
    //protected fun checkUrlExisting(guid: String?, link: String?): String? {
    //    return if (guid != null) {
    //        if (guid.contains("http")) {
    //            guid
    //        } else link
    //    } else link
    //}

    //protected suspend fun List<Article>.convertToCachedNews() =
    //    mutableListOf<CachedArticle>().also { list ->
    //        this.forEach {
    //            list.add(
    //                CachedArticle(
    //                    title = it.title!!,
    //                    image = it.image,
    //                    url = checkUrlExisting(it.guid, it.link),
    //                    source = it.sourceName,
    //                    content = it.content,
    //                    type = "news",
    //                    pubDate = it.pubDate,
    //                    cached = checkNewsInCache(it.title!!)
    //                )
    //            )
    //        }
    //    }

}


