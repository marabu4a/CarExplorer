package com.example.carexplorer.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.carexplorer.R
import com.example.carexplorer.data.model.Category
import com.example.carexplorer.repository.remote.CategoriesRepository
import com.example.carexplorer.view.CategoriesView
import kotlinx.coroutines.*
import javax.inject.Inject

@InjectViewState
class CategoriesPresenter @Inject constructor(
    repository: CategoriesRepository
) : MvpPresenter<CategoriesView>() {
    private val presenterJob = Job()

    private val BASE_URL = "https://my-project-id-326ba.firebaseio.com/.json/"
    private val categoriesRepository = repository
    private var categories : List<Category> = mutableListOf()
    fun loadCategories() {
        try {
        CoroutineScope(Dispatchers.Main + presenterJob).launch {
            viewState.startLoading()
            withContext(Dispatchers.IO) {
                categories =  categoriesRepository.getCategories(BASE_URL)
            }
            viewState.getListCategories(categories)
            viewState.endLoading()
        }
        } catch (e : Exception) {
            viewState.showMessage(R.string.errorLoadingCategories)
        }
    }

    fun stopWork() {
        presenterJob.cancel()
    }
}