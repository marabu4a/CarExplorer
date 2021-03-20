package com.example.carexplorer.presenter

import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.data.model.Source
import com.example.carexplorer.data.model.retrofit.usecase.news.GetNewsUseCase
import com.example.carexplorer.repository.cache.ContentCache
import com.example.carexplorer.ui.base.ErrorHandler
import com.example.carexplorer.view.RecentFeedView
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import moxy.InjectViewState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@AutoFactory
@InjectViewState
class RecentNewsFeedPresenter @Inject constructor(
    @Provided private val useCase: GetNewsUseCase,
    @Provided private val cachedDb: ContentCache,
    @Provided override val errorHandler: ErrorHandler
) : NewsPresenter<RecentFeedView>(cachedDb, errorHandler) {

    private var articles: MutableList<CachedArticle>? = null

    private val dateTimeStrToLocalDateTime: (CachedArticle?) -> LocalDateTime = {
        LocalDateTime.parse(it?.pubDate, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
    }

    fun fetchFeed(sources: List<Source>) {
        if (articles != null) return
        viewState.showLoading()
        sources.map {
            useCase.onObtain { params, deferred ->
                viewState.hideLoading()
                if (articles == null) {
                    articles = mutableListOf<CachedArticle>()
                }
                articles?.addAll(deferred.await().convertToCachedNews())
                viewState.showRecentFeed(articles!!)
            }.execute(GetNewsUseCase.Params(it.url, it.title), false)
        }
//        articles =
//            articles.sortedByDescending(dateTimeStrToLocalDateTime) as MutableList<CachedArticle>

    }

    override fun onDestroy() {
        super.onDestroy()
        articles = null
    }
}




