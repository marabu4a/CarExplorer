package com.example.carexplorer.view

import com.example.carexplorer.data.model.Category
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType


@StateStrategyType(AddToEndSingleStrategy::class)
interface CategoriesView : LoadingView {
    fun showMessage(textResource: Int)
    fun getListCategories(listCategories: List<Category>)
}