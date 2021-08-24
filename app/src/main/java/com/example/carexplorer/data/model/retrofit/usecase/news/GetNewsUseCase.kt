package com.example.carexplorer.data.model.retrofit.usecase.news

import com.example.carexplorer.data.model.enities.News
import com.example.carexplorer.data.model.enities.toNews
import com.example.carexplorer.data.model.retrofit.service.ApiService
import com.example.carexplorer.helpers.flow.UseCase
import timber.log.Timber
import javax.inject.Inject

class GetSourceNewsUseCase @Inject constructor(
    private val apiService: ApiService
) : UseCase<GetSourceNewsUseCase.Params, List<News>> {

    override suspend fun execute(params: Params): List<News> {
        val result = apiService.getNewsBySource(params.name)
        Timber.e(result.toString())
        if (result.isSuccessful) {
            return result.body()?.news.orEmpty().mapNotNull { it.toNews() }
        }
        return mutableListOf()
    }


    data class Params(
        val name: String
    )
}