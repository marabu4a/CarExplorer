package com.example.carexplorer.ui.activity

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface AppView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateNavbarVisibility(isVisibleOnCurrentScreen: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun resetTabs(exceptProfile: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showStartScreen()
}