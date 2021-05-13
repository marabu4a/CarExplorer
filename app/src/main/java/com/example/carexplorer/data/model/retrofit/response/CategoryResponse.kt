package com.example.carexplorer.data.model.retrofit.response

import com.example.carexplorer.data.model.enities.Category

class CategoriesResponse(
    success: Int,
    message: String,
    val categories: List<Category>
) : BaseResponse(success, message)