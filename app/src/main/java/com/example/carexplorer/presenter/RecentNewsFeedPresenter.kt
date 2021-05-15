package com.example.carexplorer.presenter

import com.example.carexplorer.data.model.enities.News
import com.example.carexplorer.data.model.retrofit.usecase.news.GetRecentNewsUseCase
import com.example.carexplorer.repository.cache.NewsCache
import com.example.carexplorer.ui.base.ErrorHandler
import com.example.carexplorer.view.RecentFeedView
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import moxy.InjectViewState
import timber.log.Timber
import javax.inject.Inject

@AutoFactory
@InjectViewState
class RecentNewsFeedPresenter @Inject constructor(
    @Provided private val getRecentNewsUseCase: GetRecentNewsUseCase,
    @Provided private val newsCache: NewsCache,
    @Provided override val errorHandler: ErrorHandler
) : NewsPresenter<RecentFeedView>(newsCache, errorHandler) {

    private var recentNews: MutableList<News>? = null

    fun fetchFeed() {
        if (recentNews != null) return
        viewState.showLoading()

        getRecentNewsUseCase.onObtain { params, deferred ->
            viewState.hideLoading()
            if (recentNews == null) {
                recentNews = mutableListOf<News>()
            }
            recentNews?.addAll(deferred.await() as List<News>)
            Timber.e(recentNews?.size.toString())
            viewState.showRecentFeed(recentNews.orEmpty().map { it.copy(isFavorite = checkCachedNews(it.title)) })
        }.execute(Unit, true)
        //        articles =
        //            articles.sortedByDescending(dateTimeStrToLocalDateTime) as MutableList<CachedArticle>

    }

    override fun onDestroy() {
        super.onDestroy()
        recentNews = null
    }
}




