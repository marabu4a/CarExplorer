package com.example.carexplorer.presenter
import com.example.carexplorer.data.model.Category

import com.example.carexplorer.data.model.retrofit.usecase.categories.GetCategoriesUseCase
import com.example.carexplorer.helpers.flow.execute
import com.example.carexplorer.ui.base.ErrorHandler
import com.example.carexplorer.view.CategoriesView
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import moxy.InjectViewState
import javax.inject.Inject

@AutoFactory
@InjectViewState
class CategoriesPresenter @Inject constructor(
    @Provided private val getCategoriesUseCase: GetCategoriesUseCase,
    @Provided override val errorHandler: ErrorHandler
) : BasePresenter<CategoriesView>(errorHandler) {

    private var categories: List<Category>? = null

    fun loadCategories() {
        if (categories != null) return
        viewState.showLoading()
        getCategoriesUseCase.onObtain { _, deferred ->
            categories = deferred.await().apply {
                viewState.hideLoading()
                viewState.getListCategories(this)
            }
        }.execute()
    }

    override fun onDestroy() {
        super.onDestroy()
        categories = null
    }

}