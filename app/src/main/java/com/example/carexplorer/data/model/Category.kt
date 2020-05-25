package com.example.carexplorer.data.model

import com.google.gson.annotations.SerializedName


data class Category(
    @SerializedName("name")
    val name : String,
    @SerializedName("image")
    val image : String,
    @SerializedName("info")
    val info : String,
    @SerializedName("articles")
    val entries : List<Entry>
)