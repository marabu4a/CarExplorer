package com.example.carexplorer.data.model.retrofit.usecase.news

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.carexplorer.helpers.flow.UseCase
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val rssParser: Parser
) : UseCase<GetNewsUseCase.Params, List<Article>> {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun execute(params: Params): List<Article> {
        return rssParser.getChannel(params.rssUrl).articles.onEach {
            it.sourceName = params.sourceTitle
            it.pubDate = parseDate(it.pubDate)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseDate(pubDate: String?): String? {
        return try {
            val sourceSdf = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
            val date = sourceSdf.parse(pubDate)
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            sdf.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            pubDate
        }
    }

    data class Params(
        val rssUrl: String,
        val sourceTitle: String
    )
}