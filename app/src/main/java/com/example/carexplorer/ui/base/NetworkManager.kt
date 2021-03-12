package com.example.carexplorer.ui.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class NetworkManager(private val applicationContext: Context) {
    private val manager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    @Suppress("DEPRECATION")
    fun isNetworkConnected(): Boolean {
        return if (manager == null) false
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // we can also use callbacks for connection|disconnection if this criteria is not good enough
            manager.getNetworkCapabilities(manager.activeNetwork)?.run {
                hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            }
        } else {
            manager.activeNetworkInfo?.isConnected
        } == true
    }
}