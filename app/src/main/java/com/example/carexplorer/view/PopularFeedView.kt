package com.example.carexplorer.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.carexplorer.data.model.CachedArticle

@StateStrategyType(AddToEndStrategy::class)
interface PopularFeedView : MvpView {
    fun startLoading()
    fun endLoading()
    fun showPopularFeed(popularFeed: List<CachedArticle>)
    fun showMessage(textResource:Int)
}