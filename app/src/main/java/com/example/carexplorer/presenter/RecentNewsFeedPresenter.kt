package com.example.carexplorer.presenter

import androidx.paging.*
import com.example.carexplorer.data.cache.NewsCache
import com.example.carexplorer.data.model.enities.News
import com.example.carexplorer.data.model.paging.NewsPagingSource
import com.example.carexplorer.data.model.retrofit.usecase.news.GetRecentNewsUseCase
import com.example.carexplorer.helpers.convertDateTime
import com.example.carexplorer.ui.base.ErrorHandler
import com.example.carexplorer.view.RecentFeedView
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.presenterScope
import javax.inject.Inject

@AutoFactory
@InjectViewState
class RecentNewsFeedPresenter @Inject constructor(
    @Provided private val getRecentNewsUseCase: GetRecentNewsUseCase,
    @Provided private val newsCache: NewsCache,
    @Provided private val newsPagingSource: NewsPagingSource,
    @Provided override val errorHandler: ErrorHandler
) : NewsPresenter<RecentFeedView>(newsCache, errorHandler) {

    private var recentNews: MutableList<News>? = null

    private fun fetchFeedFlow(): Flow<PagingData<News>> = newsPagingFlow()
        .map { it.map { it } }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        presenterScope.launch {
            fetchFeedFlow().distinctUntilChanged().collectLatest {
                viewState.updatePage(it)
            }
        }
    }

    fun fetchFeed() {
        if (recentNews != null) return
        viewState.showLoading()

        getRecentNewsUseCase.onObtain { params, deferred ->
            viewState.hideLoading()
            if (recentNews == null) {
                recentNews = mutableListOf<News>()
            }
            recentNews?.addAll(deferred.await() as List<News>)
            viewState.showRecentFeed(
                recentNews.orEmpty().map { it.copy(isFavorite = checkCachedNews(it.title), date = it.date.convertDateTime()) })
        }.execute(Unit, true)
        //        articles =
        //            articles.sortedByDescending(dateTimeStrToLocalDateTime) as MutableList<CachedArticle>

    }

    private fun newsPagingFlow(newsPagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<News>> = Pager(
        config = newsPagingConfig,
        pagingSourceFactory = { newsPagingSource }

    ).flow.cachedIn(presenterScope)

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(
            pageSize = 5,
            enablePlaceholders = true,
            prefetchDistance = 10
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        recentNews = null
    }
}




