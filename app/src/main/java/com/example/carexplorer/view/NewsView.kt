package com.example.carexplorer.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.carexplorer.data.model.CachedArticle

@StateStrategyType(AddToEndSingleStrategy::class)
interface NewsView : MvpView {
    fun showErrorScreen(state : Boolean)
    fun startLoading()
    fun showNews(news : List<CachedArticle>)
    fun endLoading()
    fun showMessage(textResource:Int)
}