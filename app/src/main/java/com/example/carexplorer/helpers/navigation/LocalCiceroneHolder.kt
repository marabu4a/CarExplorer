package com.example.carexplorer.helpers.navigation

import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

class LocalCiceroneHolder {
    private val containers = HashMap<String, Cicerone<Router>>()

    fun getCicerone(containerTag: String) : Cicerone<Router> =
        containers.getOrPut(containerTag) {
            Cicerone.create()
        }

}