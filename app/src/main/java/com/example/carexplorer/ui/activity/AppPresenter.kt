package com.example.carexplorer.ui.activity

import com.example.carexplorer.presenter.BasePresenter
import com.example.carexplorer.ui.base.ErrorHandler
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import javax.inject.Inject
import javax.inject.Singleton


@AutoFactory
@Singleton
open class AppPresenter @Inject constructor(
    @Provided override val errorHandler: ErrorHandler
) : BasePresenter<AppView>(errorHandler) {

    private var shallNavbarBeVisible: Boolean = true

    private var isFirstAttach = true


    override fun onFirstViewAttach() {
        viewState.showStartScreen()
    }

    //override fun attachView(view: AppView?) {
    //    if (!isFirstAttach) {
    //        isFirstAttach = false
    //        viewState.showStartScreen()
    //    }
    //}

    fun setBottomNavbarVisibility(isVisible: Boolean) {
        shallNavbarBeVisible = isVisible
        updateBottomNavbarVisibility()
    }

    fun updateBottomNavbarVisibility() {
        viewState.updateNavbarVisibility(
            isVisibleOnCurrentScreen = shallNavbarBeVisible
        )
    }

}