package com.example.carexplorer.view

import com.arellomobile.mvp.MvpView

interface WebPageView : MvpView {
    fun showError(textResource:Int)
}