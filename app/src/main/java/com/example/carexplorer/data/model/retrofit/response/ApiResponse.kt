package com.example.carexplorer.data.model.retrofit.response

import com.example.carexplorer.helpers.util.ServerBaseException
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response

data class ApiResponse<T>(
    val data: T,
    val error: ApiResponseError?
)

data class ApiResponseError(
    val statusCode: Int,
    val error: String,
    val message: String
) {
//    data class Validation(
//        val source: String,
//        val keys: List<String>
//    )
}


fun ResponseBody.parseError(): ApiResponseError? =
    Gson().fromJson<ApiResponseError>(this.string(), ApiResponseError::class.java)


fun <T> Response<ApiResponse<T>>.parse(): T = if (this.isSuccessful) {
    body()?.let {
        if (it.error == null) {
            return it.data
        } else {
            throw ServerBaseException(it.error.statusCode, it.error.message)
        }
    } ?: throw IllegalArgumentException("response body cannot be empty")
} else {
    val errorResponse = errorBody()?.parseError()

    errorResponse?.let { error ->
        throw ServerBaseException(
            error.statusCode,
            error.message
        )
    } ?: throw IllegalArgumentException("error response body cannot be empty")
}

data class ApiSuccessMessage(
    val message: String
)

data class ApiResultCode(
    val code: Int
) {
    companion object {
        const val SUCCESS = 100
        const val NOT_AUTHORIZED = 105
    }
}