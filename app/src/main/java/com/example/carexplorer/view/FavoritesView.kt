package com.example.carexplorer.view

import com.example.carexplorer.data.model.enities.Favorite
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface FavoritesView : LoadingView {
    fun showContent(list: ArrayList<Favorite>)
    fun showMessage(textResource: Int)
    fun updateContent(list: ArrayList<Favorite>)
}