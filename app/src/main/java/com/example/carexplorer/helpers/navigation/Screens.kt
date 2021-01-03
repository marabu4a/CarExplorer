package com.example.carexplorer.helpers.navigation

import androidx.fragment.app.Fragment
import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.data.model.Category
import com.example.carexplorer.data.model.Source
import com.example.carexplorer.ui.fragment.*
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {

    class Article(cachedArticle: CachedArticle) :
        SupportScreen({ ArticleFragmentBuilder(cachedArticle).build() })

    class Categories() : SupportAppScreen() {
        override fun getFragment(): Fragment = CategoriesFragment()
    }

    class Favorites() : SupportAppScreen() {
        override fun getFragment(): Fragment = FavoritesFragment()
    }

    class ListArticles(category: Category) :
        SupportScreen({ ListArticlesFragmentBuilder(category).build() })

    class News(source: Source) : SupportScreen({ NewsFragmentBuilder(source).build() })

    class NewsFeed() : SupportAppScreen() {
        override fun getFragment(): Fragment? = RecentNewsFeedFragment()
    }

    class Sources() : SupportAppScreen() {
        override fun getFragment(): Fragment = SourcesNewsFragment()
    }

    class WebPage(title: String, page: String) :
        SupportScreen({ WebPageFragmentBuilder(page, title).build() })
}