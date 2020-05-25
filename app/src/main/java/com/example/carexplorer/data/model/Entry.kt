package com.example.carexplorer.data.model

import com.google.gson.annotations.SerializedName


class Entry(
    @SerializedName("title")
    val title : String,

    @SerializedName("image")
    val image : String,

    @SerializedName("text")
    val content : String
)