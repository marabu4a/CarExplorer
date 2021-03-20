package com.example.carexplorer.util

data class ServerBaseException(
    val code: Int,
    override val message: String,
) : Exception(message)