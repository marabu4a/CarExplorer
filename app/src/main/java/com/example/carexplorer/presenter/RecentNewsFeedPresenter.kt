package com.example.carexplorer.presenter

import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import com.example.carexplorer.R
import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.repository.cache.ContentCache
import com.example.carexplorer.repository.remote.NewsFeedRepository
import com.example.carexplorer.view.RecentFeedView
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@AutoFactory
@InjectViewState
class RecentNewsFeedPresenter @Inject constructor(
    @Provided private val repository : NewsFeedRepository,
    @Provided private val cache : ContentCache
) : MvpPresenter<RecentFeedView>() {
    private val presenterJob = Job()
    private val articlesCache = cache
    @RequiresApi(VERSION_CODES.O)
    private val dateTimeStrToLocalDateTime: (CachedArticle?) -> LocalDateTime = {
        LocalDateTime.parse(it?.pubDate, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
    }

    @RequiresApi(VERSION_CODES.O)
    fun handleRecentNewsFeed(articles: List<CachedArticle>) {
        try {
            var sortedArticles = mutableListOf<CachedArticle>()
            CoroutineScope(Dispatchers.Main + presenterJob).launch {
                viewState.startLoading()

                withContext(Dispatchers.IO) {
                    sortedArticles =
                        articles.sortedByDescending(dateTimeStrToLocalDateTime) as MutableList<CachedArticle>
                }

                viewState.showRecentFeed(sortedArticles)
                viewState.endLoading()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            viewState.showMessage(R.string.errorLoading)
        }
    }


    fun stopWork() {
        presenterJob.cancel()
    }

    fun saveArticle(article: CachedArticle) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                article.cached = true
                articlesCache.saveArticle(article)
                viewState.showMessage(R.string.succesfull)
            }
        }
        catch( e : Exception) {
            e.printStackTrace()
            viewState.showMessage(R.string.errorSavingToDb)
        }
    }

    fun removeArticle(article: CachedArticle) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                article.cached = false
                articlesCache.removeArticleByTitle(article.title)
                viewState.showMessage(R.string.deleted)
            }
        }
        catch(e: Exception) {
            e.printStackTrace()
            viewState.showMessage(R.string.errorDeleteFromDb)
        }

    }
}
