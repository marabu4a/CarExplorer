package com.example.carexplorer.view

import androidx.paging.PagingData
import com.example.carexplorer.data.model.enities.News
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface RecentFeedView : LoadingView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showRecentFeed(recentFeed: List<News>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showMessage(textResource: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updatePage(data: PagingData<News>)
}