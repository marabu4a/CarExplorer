package com.example.carexplorer.view

import com.arellomobile.mvp.MvpView

interface ArticleView : MvpView {
    fun showArticle(image : String,text : String)
    fun startLoading()
    fun endLoading()
}