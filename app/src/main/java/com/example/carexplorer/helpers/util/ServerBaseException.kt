package com.example.carexplorer.helpers.util

data class ServerBaseException(
    val code: Int,
    override val message: String,
) : Exception(message)