package com.example.carexplorer.data.model.enities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "articles_table")
data class Article(
    val id: Int,
    @PrimaryKey
    val title: String,
    val image: String,
    val content: String,
    val categoryName: String,
    val isFavorite : Boolean = false
) : Parcelable, Favorite


