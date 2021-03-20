package com.example.carexplorer.ui.base.mvi

interface ViewState

interface MviBaseFragment<S : ViewState> {

    suspend fun render(state: S)
}