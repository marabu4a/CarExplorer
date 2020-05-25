package com.example.carexplorer.view

import com.arellomobile.mvp.MvpView
import com.example.carexplorer.data.model.CachedArticle

interface FavoritesView : MvpView {
    fun startLoading()
    fun showContent(list : List<CachedArticle>)
    fun endLoading()
    fun dismissBottomSheet(animate : Boolean)
    fun showMessage(textResource:Int)
}