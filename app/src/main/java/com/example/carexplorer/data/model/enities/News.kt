package com.example.carexplorer.data.model.enities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.carexplorer.helpers.isNullOrNotEmpty
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
) : Parcelable, Favorite

fun NewsNw.toNews(): News? {
    return News(
        id = id ?: return null,
        title = title?.isNullOrNotEmpty() ?: return null,
        description = description,
        image = image?.isNullOrNotEmpty() ?: return null,
        date = date?.isNullOrNotEmpty() ?: return null,
        link = link?.isNullOrNotEmpty() ?: return null,
        sourceName = sourceName?.isNullOrNotEmpty() ?: return null,
        )
}