package com.example.carexplorer.view

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SourcesView : LoadingView {
    fun showMessage(text: String)
}