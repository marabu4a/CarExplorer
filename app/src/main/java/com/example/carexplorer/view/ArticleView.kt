package com.example.carexplorer.view


import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ArticleView : LoadingView {
    fun showArticle(image: String, text: String)
}