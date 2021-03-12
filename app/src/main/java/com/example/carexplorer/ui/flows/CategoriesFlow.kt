package com.example.carexplorer.ui.flows

import com.example.carexplorer.helpers.navigation.Screens
import com.example.carexplorer.ui.FlowFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class CategoriesFlow : FlowFragment() {
    override val flowName: String = "Categories"

    override fun getLaunchScreen(): SupportAppScreen = Screens.Categories()
}