package com.example.carexplorer.helpers

import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.data.model.Entry
import com.prof.rssparser.Article

object TypeConverter {
    fun convertToCacheArticle(article : Article) : CachedArticle {
        return CachedArticle(
            title = article.title!!,
            image =article.image!!,
            url = article.link!!,
            pubDate = article.pubDate!!,
            source = article.sourceName!!,
            type = "news",
            content = "")
    }

    fun convertToCacheArticle(entry: Entry) : CachedArticle {
        return CachedArticle(
            title = entry.title,
            image = entry.image,
            content = entry.content,
            url = "",
            source = "",
            pubDate = "",
            type = "entry")
    }

    fun convertToArticle(cachedArticle: CachedArticle) : Article {
        return Article(
            title = cachedArticle.title,
            image = cachedArticle.image,
            link = cachedArticle.url,
            pubDate = cachedArticle.pubDate,
            sourceName = cachedArticle.source
        )
    }

    fun converttoEntry(cachedArticle: CachedArticle) : Entry {
        return Entry(
            title = cachedArticle.title,
            image = cachedArticle.image!!,
            content = cachedArticle.content!!
        )
    }


}