package com.example.carexplorer.data.model.enities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    @SerializedName("category_id")
    val id: Int,
    @SerializedName("category_name")
    val name: String,
    @SerializedName("category_image")
    val image: String,
    @SerializedName("category_description")
    val description: String
) : Parcelable