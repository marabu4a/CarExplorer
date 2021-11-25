package com.example.carexplorer.data.model.retrofit.service

import com.example.carexplorer.data.model.retrofit.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    companion object {
        const val CATEGORIES = "getCategories.php"
        const val SOURCES = "getSources.php"
        const val ARTICLES_BY_CATEGORY = "getArticlesByCategory.php"
        const val NEWS_BY_SOURCE = "getNewsBySource.php"
        const val RANDOM_NEWS = "getRandomNews.php"
        const val POPULAR_NEWS = "getPopularNews.php"
        const val NEWS_PER_PAGE = "getNewsPerPage.php"
    }

    @GET(CATEGORIES)
    suspend fun getCategories(): Response<CategoriesResponse>

    @GET(SOURCES)
    suspend fun getSources(): Response<SourcesResponse>

    @GET(ARTICLES_BY_CATEGORY)
    suspend fun getArticlesByCategory(@Query("category_name") category: String): Response<ArticlesResponse>

    @GET(NEWS_BY_SOURCE)
    suspend fun getNewsBySource(@Query("source_name") source: String): Response<NewsResponse>

    @GET(RANDOM_NEWS)
    suspend fun getRandomNews(): Response<NewsResponse>

    @GET(POPULAR_NEWS)
    suspend fun getPopularNews(): Response<NewsResponse>

    @FormUrlEncoded
    @POST(NEWS_PER_PAGE)
    suspend fun getNewsPerPage(
        @FieldMap params: Map<String, Int>
    ): Response<NewsPagingResponse>

}