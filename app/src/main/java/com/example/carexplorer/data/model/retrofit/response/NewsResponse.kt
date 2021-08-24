package com.example.carexplorer.data.model.retrofit.response

import com.example.carexplorer.data.model.enities.NewsNw

class NewsResponse(
    success: Int,
    message: String,
    val news: List<NewsNw>
) : BaseResponse(success, message) {
}