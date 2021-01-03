
package com.example.carexplorer.presenter

import com.example.carexplorer.R
import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.data.model.Source
import com.example.carexplorer.repository.cache.ContentCache
import com.example.carexplorer.repository.remote.NewsFeedRepository
import com.example.carexplorer.view.PopularFeedView
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import com.prof.rssparser.Article
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AutoFactory
@InjectViewState
class RandomNewsFeedPresenter @Inject constructor(
    @Provided private val repository : NewsFeedRepository,
    @Provided private val cache : ContentCache
) : MvpPresenter<PopularFeedView>() {
    private var presenterJob = Job()
    private val newsFeedRepository = repository
    private val articlesCache = cache
    private val listArticles : MutableList<Article> = mutableListOf()

    fun handleFeed(sourcesList: List<Source>) = try {
        CoroutineScope(Dispatchers.Main).launch {
            viewState.startLoading()

            try {
                withContext(Dispatchers.IO) {
                    fetchNewsFeed(sourcesList)
                }

            } catch (e : Exception) {
                viewState.showMessage(R.string.errorLoading)
                e.printStackTrace()
                listArticles.shuffle()
            }
            listArticles.shuffle()
            viewState.showPopularFeed(convertNews(listArticles)!!)
            viewState.endLoading()
        }
    } catch (e : Exception) {
        e.printStackTrace()
        viewState.showMessage(R.string.errorLoadingNews)
    }

    private suspend fun fetchNewsFeed(sourcesList: List<Source>)  {
        sourcesList.forEach { source ->
            newsFeedRepository.handleNewsFeed(source.url,sourceTitle = source.title).forEach { article ->
                article.pubDate = parseDate(article.pubDate)
                listArticles.add(article)
            }
        }
    }

    private fun parseDate(pubDate : String?) : String? {
        return try {
            val sourceSimpleDateFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH)
            val date = sourceSimpleDateFormat.parse(pubDate)

            val parsedDate = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            parsedDate.format(date)

        } catch (e: ParseException) {
            e.printStackTrace()
            pubDate
        }
    }

    private suspend fun convertNews(list : List<Article>) : List<CachedArticle>? {
        val listCachedArticles : MutableList<CachedArticle> = mutableListOf()
        list.forEach {
            listCachedArticles.add(
                CachedArticle(
                    title = it.title!!,
                    image = it.image,
                    url = it.link,
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

    private suspend fun checkCachedNews(newsTitle : String) : Boolean {
        var isCached = false
        withContext(Dispatchers.IO) {
            if (articlesCache.getArticleByTitle(newsTitle) != null) {
                isCached = true
            }
        }
        return isCached
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

    fun stopWork() {
        presenterJob.cancel()
    }

}