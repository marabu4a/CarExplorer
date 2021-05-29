package com.example.carexplorer.ui.base

import com.example.carexplorer.helpers.util.NetworkManager

interface ErrorHandler {
    //val notifications: Notifications
    val networkManager: NetworkManager
    fun proceed(error: Throwable): String?
}