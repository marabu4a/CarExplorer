package com.example.carexplorer.ui.base

interface ErrorHandler {
    //val notifications: Notifications
    val networkManager: NetworkManager
    fun proceed(error: Throwable): String?
}