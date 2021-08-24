package com.example.carexplorer.data.model.enities

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class ArticleNw(
    @SerializedName("article_id")
    val id: Int,
    @PrimaryKey
    @SerializedName("article_name")
    val title: String,
    @SerializedName("article_image")
    val image: String,
    @SerializedName("article_content")
    val content: String,
    @SerializedName("article_category_name")
    val categoryName: String
)

fun ArticleNw.toArticle(): Article = Article(
    id = id,
    title = title,
    image = image,
    content = content,
    categoryName = categoryName
)