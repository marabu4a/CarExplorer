package com.example.carexplorer.data.model.retrofit.usecase.news

import com.example.carexplorer.data.model.enities.News
import com.example.carexplorer.data.model.enities.toNews
import com.example.carexplorer.data.model.retrofit.service.ApiService
import com.example.carexplorer.helpers.flow.UseCase
import javax.inject.Inject

class GetRandomNewsUseCase @Inject constructor(
    private val apiService: ApiService
) : UseCase<Unit, List<News>> {

    override suspend fun execute(params: Unit): List<News> {
        val result = apiService.getRecentNews()

        if (result.isSuccessful) {
            return result.body()?.news.orEmpty().mapNotNull { it.toNews() }
        }
        return mutableListOf()
    }

}