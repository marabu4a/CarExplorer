package com.example.carexplorer.data.model.retrofit.service

import com.example.carexplorer.data.model.Category
import com.example.carexplorer.data.model.Source
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {

    @GET
    suspend fun getCategories(@Url url:String) : List<Category>

    @GET
    suspend fun getSources(@Url url:String) : List<Source>

}