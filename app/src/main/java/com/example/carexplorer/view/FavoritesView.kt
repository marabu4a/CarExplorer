package com.example.carexplorer.view

import com.example.carexplorer.data.model.CachedArticle
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType


@StateStrategyType(AddToEndSingleStrategy::class)
interface FavoritesView : BaseView {
    fun showContent(list : List<CachedArticle>)
    fun dismissBottomSheet(animate : Boolean)
    fun showMessage(textResource:Int)
}