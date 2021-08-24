package com.example.carexplorer.helpers.navigation

import androidx.fragment.app.Fragment
import com.example.carexplorer.ui.flows.CategoriesFlow
import com.example.carexplorer.ui.flows.FavoritesFlow
import com.example.carexplorer.ui.flows.NewsFlow
import com.example.carexplorer.ui.flows.SourcesFlow
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Flows {

    object SourcesScreenFlow : SupportAppScreen() {
        override fun getFragment(): Fragment = SourcesFlow()
    }

    object FavoritesScreenFlow : SupportAppScreen() {
        override fun getFragment(): Fragment = FavoritesFlow()
    }

    object CategoriesScreenFlow : SupportAppScreen() {
        override fun getFragment(): Fragment = CategoriesFlow()
    }

    object NewsScreenFlow : SupportAppScreen() {
        override fun getFragment(): Fragment = NewsFlow()
    }
}