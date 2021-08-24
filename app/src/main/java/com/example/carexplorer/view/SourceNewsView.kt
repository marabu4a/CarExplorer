package com.example.carexplorer.view

import com.example.carexplorer.data.model.enities.News
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SourceNewsView : LoadingView {
    fun showErrorScreen(state: Boolean)
    fun showNews(news: List<News>)
    fun showMessage(textResource: Int)
}