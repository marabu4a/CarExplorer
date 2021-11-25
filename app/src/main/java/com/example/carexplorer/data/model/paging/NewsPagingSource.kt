package com.example.carexplorer.data.model.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.carexplorer.data.model.enities.News
import com.example.carexplorer.data.model.enities.toNews
import com.example.carexplorer.data.model.retrofit.service.ApiService
import javax.inject.Inject

class NewsPagingSource @Inject constructor(
    private val api: ApiService
) : PagingSource<Int, News>() {

    override fun getRefreshKey(state: PagingState<Int, News>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPageIndex = state.pages.indexOf(state.closestPageToPosition(anchorPosition))
            state.pages.getOrNull(anchorPageIndex + 1)?.prevKey ?: state.pages.getOrNull(anchorPageIndex - 1)?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        val nextPage = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val bodyResponse = HashMap<String, Int>().apply {
                put(NEWS_PAGING_BODY_RESPONSE_PAGE, nextPage)
                put(NEWS_PAGING_BODY_RESPONSE_LOAD_SIZE, params.loadSize)
            }
            val responseNw = api.getNewsPerPage(bodyResponse).body()
            val response = responseNw?.news?.mapNotNull {
                it.toNews()
            } ?: mutableListOf()
            LoadResult.Page(
                data = response,
                prevKey = if (nextPage == DEFAULT_PAGE_INDEX) null else nextPage - 1,
                nextKey = if (response.isEmpty()) null else nextPage + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    companion object {
        private const val DEFAULT_PAGE_INDEX = 1
        private const val NEWS_PAGING_BODY_RESPONSE_PAGE = "page"
        private const val NEWS_PAGING_BODY_RESPONSE_LOAD_SIZE = "load_size"
    }

}