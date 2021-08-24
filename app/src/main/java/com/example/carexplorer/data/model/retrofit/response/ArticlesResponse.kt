package com.example.carexplorer.data.model.retrofit.response

import com.example.carexplorer.data.model.enities.ArticleNw

class ArticlesResponse(
    success: Int,
    message: String,
    val articles: List<ArticleNw>
) : BaseResponse(success, message)