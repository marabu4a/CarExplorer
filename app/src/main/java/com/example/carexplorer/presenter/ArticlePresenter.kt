package com.example.carexplorer.presenter

import com.example.carexplorer.ui.base.ErrorHandler
import com.example.carexplorer.view.ArticleView
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.cicerone.Router

@AutoFactory
@InjectViewState
class ArticlePresenter(
    private val router: Router,
    @Provided override val errorHandler: ErrorHandler
) : BasePresenter<ArticleView>(errorHandler) {
    private val presenterJob = Job()

    override fun onBackPressed() {
        router.exit()
    }

    fun loadArticle(image: String, text: String) {
        CoroutineScope(Dispatchers.Main + presenterJob).launch {
            viewState.showArticle(image, text)
        }
    }
}