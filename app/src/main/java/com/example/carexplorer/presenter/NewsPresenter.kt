package com.example.carexplorer.presenter

import android.os.Build
import androidx.annotation.RequiresApi
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.carexplorer.R
import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.repository.cache.ContentCache
import com.example.carexplorer.repository.remote.NewsFeedRepository
import com.example.carexplorer.view.NewsView
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import kotlinx.coroutines.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@InjectViewState
class NewsPresenter @Inject constructor(
    cache : ContentCache,
    repository: NewsFeedRepository
) : MvpPresenter<NewsView>() {
    private val newsFeedRepository = repository
    private val articlesCache = cache
    private val presenterJob = Job()
    private val parser = Parser()
    private var articles = listOf<Article>()
    @RequiresApi(Build.VERSION_CODES.O)
    fun loadNews(url: String,sourceTitle : String) {
        try {
            CoroutineScope(Dispatchers.Main + presenterJob).launch {
                viewState.startLoading()
                try {
                    withContext(Dispatchers.IO) {
                        articles = newsFeedRepository.handleNewsFeed(url,sourceTitle)
                        articles.forEach {
                            it.pubDate = parseDate(it.pubDate)
                        }
                    }
                } catch (e:Exception) {
                    e.printStackTrace()
                    viewState.showErrorScreen(true)
                    viewState.showMessage(R.string.errorLoading)
                }
                if (!articles.isNullOrEmpty()) {
                    viewState.showNews(convertNews(articles)!!)
                }
                else {
                    viewState.showErrorScreen(true)
                }
                viewState.endLoading()
            }
        } catch (e : Exception) {
            e.printStackTrace()
            viewState.showMessage(R.string.errorLoading)
        }

    }
    fun stopWork() {
        presenterJob.cancel()
    }

    private suspend fun convertNews(list : List<Article>) : List<CachedArticle>? {
        val listCachedArticles : MutableList<CachedArticle> = mutableListOf()
        list.forEach {
            listCachedArticles.add(
                CachedArticle(
                    title = it.title!!,
                    image = it.image,
                    url = checkUrl(it.guid,it.link),
                    source = it.sourceName,
                    content = it.content,
                    type = "news",
                    pubDate = it.pubDate,
                    cached = checkCachedNews(it.title!!)
                )
            )
        }
        return listCachedArticles
    }

    private fun checkUrl(guid : String?,link : String?) : String? {
        return if (guid != null) {
            if (guid.contains("http")) {
                guid
            } else link
        } else link
    }

    private suspend fun checkCachedNews(newsTitle : String) : Boolean {
        var isCached = false
        withContext(Dispatchers.IO) {
            if (articlesCache.getArticleByTitle(newsTitle) != null) {
                isCached = true
            }
        }
        return isCached
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseDate(pubDate : String?) : String? {
        return try {

            val sourceSdf = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
            val date = sourceSdf.parse(pubDate)


            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            sdf.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            pubDate
        }
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


