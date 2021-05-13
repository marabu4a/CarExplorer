package com.example.carexplorer.data.model.retrofit.usecase.categories

import com.example.carexplorer.data.model.enities.Category
import com.example.carexplorer.data.model.retrofit.service.ApiService
import com.example.carexplorer.helpers.flow.UseCase
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val api: ApiService
) : UseCase<Unit, List<Category>> {

    override suspend fun execute(params: Unit): List<Category> {
        val result = api.getCategories()

        if (result.isSuccessful) {
            return result.body()?.categories!!
        }
        return mutableListOf()
    }
}