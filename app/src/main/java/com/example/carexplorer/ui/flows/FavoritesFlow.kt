package com.example.carexplorer.ui.flows

import com.example.carexplorer.helpers.navigation.Screens
import com.example.carexplorer.ui.base.FlowFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class FavoritesFlow : FlowFragment() {
    override val flowName: String = "Favorites"

    override fun getLaunchScreen(): SupportAppScreen = Screens.Favorites()
}