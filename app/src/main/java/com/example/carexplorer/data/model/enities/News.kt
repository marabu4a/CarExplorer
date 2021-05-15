package com.example.carexplorer.data.model.enities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "news_table")
@Parcelize
data class News(
    val id: Int,
    @PrimaryKey
    val title: String,
    val description: String?,
    val image: String,
    val date: String,
    val link: String,
    val sourceName: String,
    val isFavorite: Boolean = false
) : Parcelable

fun NewsNw.toNews(): News? {
    return News(
        id = id ?: return null,
        title = title ?: return null,
        description = description,
        image = image ?: return null,
        date = date ?: return null,
        link = link ?: return null,
        sourceName = sourceName ?: return null,
        )
}