package com.example.carexplorer.view

import com.arellomobile.mvp.MvpView
import com.example.carexplorer.data.model.Source

interface SourcesView : MvpView {
    fun startLoading()
    fun showSources(sources : List<Source>)
    fun endLoading()
    fun showMessage(textResource:Int)
}