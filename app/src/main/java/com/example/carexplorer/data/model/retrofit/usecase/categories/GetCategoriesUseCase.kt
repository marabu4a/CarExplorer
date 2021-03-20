package com.example.carexplorer.data.model.retrofit.usecase.categories

import com.example.carexplorer.data.model.Category
import com.example.carexplorer.data.model.retrofit.service.CategoriesApiService
import com.example.carexplorer.helpers.flow.UseCase
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val api: CategoriesApiService
) : UseCase<Unit, List<Category>> {

    override suspend fun execute(params: Unit): List<Category> {
        val result = api.getCategories()

        if (result.isSuccessful) {
            return api.getCategories().body()!!
        }
        return mutableListOf()
    }
}