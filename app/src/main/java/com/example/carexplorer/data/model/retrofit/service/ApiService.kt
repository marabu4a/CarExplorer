package com.example.carexplorer.data.model.retrofit.service

import com.example.carexplorer.data.model.retrofit.response.ArticlesResponse
import com.example.carexplorer.data.model.retrofit.response.CategoriesResponse
import com.example.carexplorer.data.model.retrofit.response.NewsResponse
import com.example.carexplorer.data.model.retrofit.response.SourcesResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    companion object {
        const val CATEGORIES = "getCategories.php"
        const val SOURCES = "getSources.php"
        const val ARTICLES_BY_CATEGORY = "getArticlesByCategory.php"
        const val NEWS_BY_SOURCE = "getNewsBySource.php"
        const val RECENT_NEWS = "getRecentNews.php"
    }

    @GET(CATEGORIES)
    suspend fun getCategories(): Response<CategoriesResponse>

    @GET(SOURCES)
    suspend fun getSources(): Response<SourcesResponse>

    @GET(ARTICLES_BY_CATEGORY)
    suspend fun getArticlesByCategory(category: String): Response<ArticlesResponse>

    @GET(NEWS_BY_SOURCE)
    suspend fun getNewsBySource(source: String): Response<NewsResponse>

    @GET(RECENT_NEWS)
    suspend fun getRecentNews(): Response<NewsResponse>

}