package com.example.carexplorer.repository.remote

import com.example.carexplorer.data.model.Category
import com.example.carexplorer.data.model.Source
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {

    companion object {
        const val PARAM_LINK_NEWS = "news"
        const val PARAM_CATEGORIES = "categories"
        const val PARAM_TITLE_ARTICLE = "title"
        const val PARAM_TEXT_ARTICLE = "text"
        const val PARAM_NAME_CATEGORY = "name"
        const val PARAM_IMAGE_ARTICLE = "image"
    }


    @GET
    suspend fun getCategories(@Url url:String) : List<Category>

    @GET
    suspend fun getSources(@Url url:String) : List<Source>

}