package com.example.carexplorer.helpers.navigation

import androidx.fragment.app.Fragment
import com.example.carexplorer.data.model.enities.Article
import com.example.carexplorer.data.model.enities.Category
import com.example.carexplorer.data.model.enities.Source
import com.example.carexplorer.ui.fragment.*
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {

    class ArticleScreen(article: Article) :
        SupportScreen({ ArticleFragmentBuilder(article).build() })

    class CategoriesScreen() : SupportAppScreen() {
        override fun getFragment(): Fragment = CategoriesFragment()
    }

    class FavoritesScreen() : SupportAppScreen() {
        override fun getFragment(): Fragment = FavoritesFragment()
    }

    class ListArticlesScreen(category: Category) :
        SupportScreen({ ListArticlesFragmentBuilder(category).build() })

    class SourceNewsScreen(source: Source) : SupportScreen({ SourceNewsFragmentBuilder(source).build() })

    class NewsFeedScreens() : SupportAppScreen() {
        override fun getFragment(): Fragment = RecentNewsFeedFragment()
    }

    class SourcesScreen() : SupportAppScreen() {
        override fun getFragment(): Fragment = SourcesFragment()
    }

    class WebPageScreen(title: String, page: String) :
        SupportScreen({ WebPageFragmentBuilder(page, title).build() })
}