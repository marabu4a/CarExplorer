package com.example.carexplorer.data.model.enities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
class Article(
    @SerializedName("article_id")
    val id: Int,
    @SerializedName("article_name")
    val title: String,
    @SerializedName("article_image")
    val image: String,
    @SerializedName("article_content")
    val content: String,
    @SerializedName("article_category_name")
    val categoryName: String
) : Parcelable