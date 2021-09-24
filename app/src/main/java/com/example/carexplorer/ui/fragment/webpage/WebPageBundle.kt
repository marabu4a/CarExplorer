package com.example.carexplorer.ui.fragment.webpage

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WebPageBundle(
    val page: String,
    val title: String,
    val image: String
) : Parcelable