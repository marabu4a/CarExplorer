package com.example.carexplorer.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
class Entry(
    @SerializedName("title")
    val title : String,

    @SerializedName("image")
    val image : String,

    @SerializedName("text")
    val content : String
) : Parcelable