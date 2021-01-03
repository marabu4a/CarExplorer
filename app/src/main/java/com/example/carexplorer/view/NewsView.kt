package com.example.carexplorer.view


import com.example.carexplorer.data.model.CachedArticle
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface NewsView : BaseView {
    fun showErrorScreen(state : Boolean)
    fun showNews(news : List<CachedArticle>)
    fun showMessage(textResource:Int)
}