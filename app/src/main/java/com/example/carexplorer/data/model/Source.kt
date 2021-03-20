package com.example.carexplorer.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Source(
    val title: String,
    val image: String,
    val url : String
) : Parcelable
