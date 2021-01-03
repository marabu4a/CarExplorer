package com.example.carexplorer.view

import com.example.carexplorer.data.model.CachedArticle
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface RecentFeedView : BaseView {
    fun showRecentFeed(recentFeed: List<CachedArticle>)
    fun showMessage(textResource:Int)
}