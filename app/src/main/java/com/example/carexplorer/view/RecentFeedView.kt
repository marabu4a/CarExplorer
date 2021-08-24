package com.example.carexplorer.view

import com.example.carexplorer.data.model.enities.News
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface RecentFeedView : LoadingView {
    fun showRecentFeed(recentFeed: List<News>)
    fun showMessage(textResource: Int)
}