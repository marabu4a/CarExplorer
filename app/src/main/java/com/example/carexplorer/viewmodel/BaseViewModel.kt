package com.example.carexplorer.viewmodel

import androidx.lifecycle.ViewModel
import com.example.carexplorer.ui.base.ErrorHandler
import timber.log.Timber

abstract class BaseViewModel(
    protected open val errorHandler: ErrorHandler
) : ViewModel() {

    protected open var showErrorOnlyInLog = false

    open fun onError(throwable: Throwable) {
        Timber.d("BaseViewModel.kt.onError: ${throwable.message}")
        Timber.e(throwable)

        val message = errorHandler.proceed(throwable)
        message?.let {
            if (it.isNotEmpty() && !showErrorOnlyInLog) {
                // errorHandler.notifications.showRequestError(message)
            }
        }
    }

}