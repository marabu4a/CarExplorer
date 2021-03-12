package com.example.carexplorer.ui.base

import com.example.carexplorer.helpers.flow.ExecutorsFactory
import com.example.carexplorer.helpers.flow.UseCase
import com.example.carexplorer.helpers.flow.UseCaseExecutorsFactoryProvider
import kotlinx.coroutines.Deferred
import moxy.MvpPresenter
import moxy.MvpView
import org.xml.sax.ErrorHandler
import timber.log.Timber

abstract class BasePresenter<V : MvpView>(
    factoryProvider: UseCaseExecutorsFactoryProvider,
    protected val errorHandler: ErrorHandler
) : MvpPresenter<V>() {

//    protected val notifications = errorHandler.notifications
//    protected open var showErrorOnlyInLog = false

    open fun onBackPressed() {}

    open fun onError(throwable: Throwable) {
        Timber.e(throwable)
//        FirebaseCrashlytics.getInstance().recordException(throwable)
//
//        val message = errorHandler.proceed(throwable)
//        //todo: analyze server and client-based exception
//        message?.let {
//            if (it.isNotEmpty() && !showErrorOnlyInLog) {
//                errorHandler.notifications.showRequestError(message)
//            }
//        }
    }

    protected open val executorsFactory: ExecutorsFactory = factoryProvider.create(::onError)

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