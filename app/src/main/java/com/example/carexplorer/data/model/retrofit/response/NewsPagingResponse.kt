package com.example.carexplorer.data.model.retrofit.response

import com.example.carexplorer.data.model.enities.NewsNw
import com.google.gson.annotations.SerializedName

class NewsPagingResponse(
    success: Int,
    message: String,
    val news: List<NewsNw>,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("total_pages")
    val totalPages: Int
) : BaseResponse(success, message)