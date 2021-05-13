package com.example.carexplorer.presenter

import com.example.carexplorer.ui.base.ErrorHandler
import com.example.carexplorer.view.WebPageView
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import moxy.InjectViewState
import ru.terrakok.cicerone.Router

@AutoFactory
@InjectViewState
class WebPagePresenter(
    @Provided override val errorHandler: ErrorHandler,
    private val router: Router
) : BasePresenter<WebPageView>(errorHandler) {

    override fun onBackPressed() {
        router.exit()
    }

    fun loadUrl(url: String) {
        viewState.loadUrl(url)
    }
}