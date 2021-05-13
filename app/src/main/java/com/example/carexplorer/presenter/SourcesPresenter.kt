package com.example.carexplorer.presenter

import com.example.carexplorer.view.SourcesView
import com.google.auto.factory.AutoFactory
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject


@AutoFactory
@InjectViewState
class SourcesPresenter @Inject constructor() : MvpPresenter<SourcesView>() {

    fun startLoading() {
        viewState.showLoading()
    }

    fun stopLoading() {
        viewState.hideLoading()
    }

    fun showError(message: String?) {
        viewState.showMessage(message!!)
    }
}