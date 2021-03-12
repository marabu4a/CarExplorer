package com.example.carexplorer.presenter

import com.example.carexplorer.R
import com.example.carexplorer.data.model.Category
import com.example.carexplorer.repository.remote.CategoriesRepository
import com.example.carexplorer.view.CategoriesView
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@AutoFactory
@InjectViewState
class CategoriesPresenter @Inject constructor(
    @Provided private val repository: CategoriesRepository
) : MvpPresenter<CategoriesView>() {
    private val presenterJob = Job()

    private val BASE_URL = "https://my-project-id-326ba.firebaseio.com/.json/"
    private var categories : List<Category> = mutableListOf()
    fun loadCategories() {
        try {
        CoroutineScope(Dispatchers.Main + presenterJob).launch {
            viewState.startLoading()
            withContext(Dispatchers.IO) {
                categories = repository.getCategories(BASE_URL)
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