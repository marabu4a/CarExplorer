package com.example.carexplorer.di

import com.example.carexplorer.ui.base.ErrorHandler
import com.example.carexplorer.util.DefaultErrorHandler
import dagger.Binds
import dagger.Module

@Module
abstract class UtilModule {

    @Binds
    abstract fun bindDefaultErrorHandler(defaultErrorHandler: DefaultErrorHandler): ErrorHandler

    //TODO разобраться с инжектом провайдера
//    @Binds
//    abstract fun bindUseCaseExecutorProvider(useCaseExecutorsFactoryProvider: UseCaseExecutorsFactoryProvider) useCaseExecutorsFactoryProvider

}