package com.example.carexplorer.repository.remote

import com.example.carexplorer.data.model.Source
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import javax.inject.Inject

class NewsFeedRepositoryImpl @Inject constructor(
    private val service: ApiService,
    private val parser: Parser
) : NewsFeedRepository {
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