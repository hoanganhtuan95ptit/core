package com.simple.coreapp.data.usecase

import org.koin.core.component.KoinComponent


interface BaseUseCase<Param, Result> : KoinComponent {
    suspend fun execute(param: Param? = null): Result

    open class Param
}