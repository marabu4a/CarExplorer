package com.example.carexplorer.data.model.retrofit.usecase.articles

import com.example.carexplorer.data.model.enities.Article
import com.example.carexplorer.data.model.enities.toArticle
import com.example.carexplorer.data.model.retrofit.service.ApiService
import com.example.carexplorer.helpers.flow.UseCase
import timber.log.Timber
import javax.inject.Inject

class GetArticlesByCategoryUseCase @Inject constructor(
    private val apiService: ApiService
) : UseCase<String,List<Article>> {

    override suspend fun execute(params: String): List<Article> {
        val result = apiService.getArticlesByCategory(params)
        Timber.e(result.toString())
        if (result.isSuccessful) {
            return result.body()?.articles.orEmpty().map { it.toArticle() }
        }
        return mutableListOf()
    }
}