package com.example.carexplorer.data.model.retrofit.service

import com.example.carexplorer.data.model.enities.Category
import retrofit2.Response
import retrofit2.http.GET

interface CategoriesApiService {

    @GET(".json/")
    suspend fun getCategories(): Response<List<Category>>
}