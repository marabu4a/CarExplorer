package com.example.carexplorer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class CachedArticle(
    @PrimaryKey
    val title : String,
    var image : String? = null,
    var url : String? = null,
    var pubDate : String? = null,
    var source : String? = null,
    var type : String?= null,
    var content : String? = null,
    var cached : Boolean = false
)