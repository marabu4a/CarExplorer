package com.example.carexplorer.data.model.enities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "articles_table")
data class Article(
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
    val categoryName: String,
    val isFavorite : Boolean = false
) : Parcelable