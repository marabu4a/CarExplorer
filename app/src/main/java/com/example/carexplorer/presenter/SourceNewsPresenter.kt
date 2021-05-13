package com.example.carexplorer.presenter

import com.example.carexplorer.data.model.retrofit.usecase.news.GetNewsUseCase
import com.example.carexplorer.repository.cache.ContentCache
import com.example.carexplorer.ui.base.ErrorHandler
import com.example.carexplorer.view.SourceNewsView
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import com.prof.rssparser.Article
import moxy.InjectViewState
import javax.inject.Inject

@AutoFactory
@InjectViewState
class SourceNewsPresenter @Inject constructor(
    @Provided private val contentDb: ContentCache,
    @Provided override val errorHandler: ErrorHandler,
    @Provided private val getNewsUseCase: GetNewsUseCase
) : NewsPresenter<SourceNewsView>(contentDb, errorHandler) {

    private var sourceNews: List<Article>? = null

    fun fetchNews(url: String, sourceTitle: String) {
        if (sourceNews != null) return
        viewState.showLoading()
        executorsFactory.create(
            getNewsUseCase,
            onError = {
                viewState.hideLoading()
                viewState.showErrorScreen(true)
            },
            onObtain = { params, deferred ->
                sourceNews = deferred.await()
                viewState.hideLoading()
                viewState.showNews(sourceNews.orEmpty().convertToCachedNews())
            }
        ).execute(GetNewsUseCase.Params(url, sourceTitle))
    }

    override fun onDestroy() {
        super.onDestroy()
        sourceNews = null
    }

}