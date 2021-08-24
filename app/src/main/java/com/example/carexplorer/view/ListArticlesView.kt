package com.example.carexplorer.view

import com.example.carexplorer.data.model.enities.Article
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ListArticlesView : LoadingView {
    fun showListArticles(entries: List<Article>)
    fun showMessage(textResource: Int)
}