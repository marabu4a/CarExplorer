package com.example.carexplorer.presenter

import com.example.carexplorer.data.model.enities.News
import com.example.carexplorer.data.model.retrofit.usecase.news.GetSourceNewsUseCase
import com.example.carexplorer.repository.cache.NewsCache
import com.example.carexplorer.ui.base.ErrorHandler
import com.example.carexplorer.view.SourceNewsView
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import moxy.InjectViewState
import timber.log.Timber
import javax.inject.Inject

@AutoFactory
@InjectViewState
class SourceNewsPresenter @Inject constructor(
    @Provided private val newsCache: NewsCache,
    @Provided override val errorHandler: ErrorHandler,
    @Provided private val getNewsBySourceUseCase: GetSourceNewsUseCase
) : NewsPresenter<SourceNewsView>(newsCache, errorHandler) {

    private var sourceNews: List<News>? = null

    fun fetchNews(name: String) {
        Timber.e("$name")
        if (sourceNews != null) return
        viewState.showLoading()
        executorsFactory.create(
            getNewsBySourceUseCase,
            onError = {
                Timber.e(it)
                viewState.hideLoading()
                viewState.showErrorScreen(true)
            },
            onObtain = { params, deferred ->
                sourceNews = deferred.await()
                Timber.e(sourceNews.toString())
                viewState.hideLoading()
                viewState.showNews(sourceNews.orEmpty().map { it.copy(isFavorite = checkCachedNews(it.title)) })
            }
        ).execute(GetSourceNewsUseCase.Params(name))
    }

    override fun onDestroy() {
        super.onDestroy()
        sourceNews = null
    }

}