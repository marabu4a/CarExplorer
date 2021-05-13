package com.example.carexplorer.view

import moxy.viewstate.strategy.AddToEndSingleTagStrategy
import moxy.viewstate.strategy.StateStrategyType

interface LoadingView : BaseView {

    @StateStrategyType(AddToEndSingleTagStrategy::class, tag = "loading")
    fun showLoading()

    @StateStrategyType(AddToEndSingleTagStrategy::class, tag = "loading")
    fun hideLoading()
}