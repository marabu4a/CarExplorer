package com.example.carexplorer.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    @SerializedName("name")
    val name : String,
    @SerializedName("image")
    val image : String,
    @SerializedName("info")
    val info : String,
    @SerializedName("articles")
    val entries : List<Entry>
) : Parcelable