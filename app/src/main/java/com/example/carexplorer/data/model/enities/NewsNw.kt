package com.example.carexplorer.data.model.enities

import com.google.gson.annotations.SerializedName

data class NewsNw(
    @SerializedName("news_id")
    val id: Int?,
    @SerializedName("news_title")
    val title: String?,
    @SerializedName("news_description")
    val description: String?,
    @SerializedName("news_image")
    val image: String?,
    @SerializedName("news_date")
    val date: String?,
    @SerializedName("news_link")
    val link: String?,
    @SerializedName("news_source_name")
    val sourceName: String?
)