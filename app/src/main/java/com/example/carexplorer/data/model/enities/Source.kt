package com.example.carexplorer.data.model.enities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Source(
    @SerializedName("source_id")
    val id: Int,
    @SerializedName("source_name")
    val name: String,
    @SerializedName("source_image")
    val image: String,
    @SerializedName("source_link")
    val url: String
) : Parcelable
