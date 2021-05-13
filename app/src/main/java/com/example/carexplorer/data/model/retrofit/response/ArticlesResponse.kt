package com.example.carexplorer.data.model.retrofit.response

import com.example.carexplorer.data.model.enities.Article

class ArticlesResponse(
    success: Int,
    message: String,
    val articles: List<Article>
) : BaseResponse(success, message)