package com.example.carexplorer.view

import moxy.MvpView

interface BaseView : MvpView {
    fun startLoading()
    fun endLoading()
}