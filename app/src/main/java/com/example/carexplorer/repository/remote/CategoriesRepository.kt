package com.example.carexplorer.repository.remote

import com.example.carexplorer.data.model.Category
import retrofit2.http.Url

interface CategoriesRepository {
   suspend fun getCategories(@Url url : String) : List<Category>
}