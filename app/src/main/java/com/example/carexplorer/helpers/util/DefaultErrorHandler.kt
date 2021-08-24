package com.example.carexplorer.helpers.util

import android.content.Context
import com.example.carexplorer.R
import com.example.carexplorer.ui.base.ErrorHandler
import retrofit2.HttpException
import java.net.ConnectException
import javax.inject.Inject


class DefaultErrorHandler @Inject constructor(
    private val context: Context,
    //override val notifications: Notifications,
    override val networkManager: NetworkManager
) : ErrorHandler {

    override fun proceed(error: Throwable): String {

        return when (error) {
            is HttpException -> when (error.code()) {
                500 -> context.getString(R.string.error_server_not_available)
                400 -> error.message()
                404 -> error.message()
                else -> context.getString(
                    R.string.error_something_bad_happened_with_code,
                    error.code()
                )
            }
            is ConnectException -> error.message.orEmpty()
            else -> context.getString(R.string.error_unknown)
        }
    }
}