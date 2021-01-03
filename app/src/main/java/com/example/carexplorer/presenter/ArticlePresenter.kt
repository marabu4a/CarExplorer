package com.example.carexplorer.presenter

import com.example.carexplorer.view.ArticleView
import com.google.auto.factory.AutoFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter

@AutoFactory
@InjectViewState
class ArticlePresenter : MvpPresenter<ArticleView>() {
    private val presenterJob = Job()
    fun loadArticle(image : String,text : String) {
        CoroutineScope(Dispatchers.Main + presenterJob).launch {
            viewState.startLoading()
            viewState.showArticle(image, text)
            viewState.endLoading()
        }
    }

    fun stopWork() {
        presenterJob.cancel()
    }
}