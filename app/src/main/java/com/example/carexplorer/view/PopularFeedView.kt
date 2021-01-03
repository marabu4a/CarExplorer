package com.example.carexplorer.view
import com.example.carexplorer.data.model.CachedArticle
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface PopularFeedView : BaseView {
    fun showPopularFeed(popularFeed: List<CachedArticle>)
    fun showMessage(textResource:Int)
}