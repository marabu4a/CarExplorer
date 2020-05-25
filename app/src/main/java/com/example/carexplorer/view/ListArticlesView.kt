package com.example.carexplorer.view

import com.arellomobile.mvp.MvpView
import com.example.carexplorer.data.model.CachedArticle

interface ListArticlesView : MvpView {
    fun startLoading()
    fun showListArticles(entries : List<CachedArticle>)
    fun endLoading()
    fun showMessage(textResource:Int)
}