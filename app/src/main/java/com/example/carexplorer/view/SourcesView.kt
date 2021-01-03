package com.example.carexplorer.view

import com.example.carexplorer.data.model.Source
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SourcesView : BaseView {
    fun showSources(sources : List<Source>)
    fun showMessage(textResource:Int)
}