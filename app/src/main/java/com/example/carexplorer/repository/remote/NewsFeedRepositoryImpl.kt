package com.example.carexplorer.repository.remote

import android.content.Context
import com.example.carexplorer.data.model.Source
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import javax.inject.Inject

class NewsFeedRepositoryImpl @Inject constructor(
    private val service: ApiService,
    private val context : Context
) : NewsFeedRepository {
    private val parser = Parser.Builder()
        .context(context)
        .cacheExpirationMillis(24L * 60L * 60L * 100L)
        .build()
    override suspend fun getNewsFeed(url: String): List<Source> {
        return service.getSources(url)
    }

    override suspend fun handleNewsFeed(urlXml: String,sourceTitle : String): List<Article> {

        val newsFeed = parser.getChannel(urlXml)
        newsFeed.articles.forEach {
            it.sourceName = sourceTitle
        }
        return newsFeed.articles
    }
}