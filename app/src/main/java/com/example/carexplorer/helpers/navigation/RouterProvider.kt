package com.example.carexplorer.helpers.navigation

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router

interface RouterProvider {
    val ciceroneRouter: Router
}

val Fragment.parentRouter : Router
    get() = (parentFragment as? RouterProvider)?.ciceroneRouter
        ?: (activity as RouterProvider).ciceroneRouter