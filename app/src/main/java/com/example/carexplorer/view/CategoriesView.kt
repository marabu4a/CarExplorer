package com.example.carexplorer.view

import com.arellomobile.mvp.MvpView
import com.example.carexplorer.data.model.Category

interface CategoriesView : MvpView {
    fun startLoading()
    fun endLoading()
    fun showMessage(textResource:Int)
    fun getListCategories(listCategories : List<Category>)
}