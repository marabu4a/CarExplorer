package com.example.carexplorer.repository.remote

import com.example.carexplorer.data.model.Category
import retrofit2.http.Url
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    private val service: ApiService
) :CategoriesRepository {

     override suspend fun getCategories(@Url url : String) : List<Category> {
        return service.getCategories(url)
    }

}