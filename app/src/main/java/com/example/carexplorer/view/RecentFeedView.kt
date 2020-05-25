package com.example.carexplorer.view

import com.arellomobile.mvp.MvpView
import com.example.carexplorer.data.model.CachedArticle

interface RecentFeedView : MvpView {
    fun startLoading()
    fun endLoading()
    fun showRecentFeed(recentFeed: List<CachedArticle>)
    fun showMessage(textResource:Int)
}