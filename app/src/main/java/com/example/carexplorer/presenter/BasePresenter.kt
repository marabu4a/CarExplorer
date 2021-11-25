package com.example.carexplorer.presenter

import com.example.carexplorer.helpers.flow.ExecutorsFactory
import com.example.carexplorer.helpers.flow.UseCase
import com.example.carexplorer.helpers.flow.UseCaseExecutorsFactoryProvider
import com.example.carexplorer.ui.base.ErrorHandler
import kotlinx.coroutines.*
import moxy.MvpPresenter
import moxy.MvpView
import timber.log.Timber


abstract class BasePresenter<V : MvpView>(
    open val errorHandler: ErrorHandler
) : MvpPresenter<V>() {

    //protected val notifications = errorHandler.notifications
    //protected open var showErrorOnlyInLog = false

    protected val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    protected fun <TResult> (suspend () -> TResult).launchOnMain(onObtain: (TResult) -> Unit = {}) {
        scope.launch {
            try {
                onObtain(invoke())
            } catch (e: CancellationException) {
                // scope was cancelled in onDestroy
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        Timber.i("onFirstViewAttach ${this.javaClass.name}")
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    open fun onBackPressed() {}

    open fun onError(throwable: Throwable) {
        Timber.e(throwable)

        val message = errorHandler.proceed(throwable)
        //todo: analyze server and client-based exception
//        message?.let {
//            if (it.isNotEmpty() && !showErrorOnlyInLog) {
//                errorHandler.notifications.showRequestError(message)
//            }
//        }

    }

    protected open val executorsFactory: ExecutorsFactory =
        UseCaseExecutorsFactoryProvider.create(::onError)

    fun <TParams, TResult> UseCase<TParams, TResult>.onObtain(
        action: suspend (TParams, Deferred<TResult>) -> Unit
    ) = executorsFactory.create(
        useCase = this,
        onObtain = action
    )

    fun <TParams, TResult> UseCase<TParams, TResult>.executor() = executorsFactory.create(
        useCase = this,
        onObtain = { _, _ -> }
    )
}