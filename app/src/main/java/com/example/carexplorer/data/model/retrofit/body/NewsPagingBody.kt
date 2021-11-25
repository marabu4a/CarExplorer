package com.example.carexplorer.data.model.retrofit.body

import com.google.gson.annotations.SerializedName

data class NewsPagingBody(
    val page: Int,
    @SerializedName("load_size")
    val loadSize: Int
)