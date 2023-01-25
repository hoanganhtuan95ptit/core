package com.one.coreapp.utils.extentions

import com.one.coreapp.data.usecase.ResultState
import com.one.coreapp.data.usecase.isFailed
import com.one.coreapp.data.usecase.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

interface Task<Param, Result> {

    fun priority(): Int = 0

    suspend fun execute(param: Param): ResultState<Result>
}


suspend fun <Param, Result> List<Task<Param, Result>>.executeByPriority(param: Param): ResultState<Result> = channelFlow {


    val listDeferred = sortedByDescending {

        it.priority()
    }.map {

        async { it.execute(param) }
    }



    launch(Dispatchers.IO) {

        val outputs = arrayListOf<ResultState<Result>>()

        for (deferred in listDeferred) deferred.await().let {

            if (it.isSuccess()) {

                offerActive(it)
            }

            outputs.add(it)
        }

        if (outputs.all { it.isFailed() }) {

            offerActive(outputs.first())
        }
    }


    awaitClose {
    }
}.first()


suspend fun <Param, Result> List<Task<Param, Result>>.executeByFast(param: Param): ResultState<Result> = channelFlow {


    val listDeferred = sortedByDescending {

        it.priority()
    }.map {

        async {

            val output = it.execute(param)

            if (output.isSuccess()) {

                offerActive(output)
            }

            output
        }
    }

    launch(Dispatchers.IO) {

        val listTranslate = listDeferred.awaitAll()

        if (listTranslate.all { it.isFailed() }) {

            offerActive(listTranslate.first())
        }
    }

    awaitClose {
    }
}.first()

suspend fun <Param, Result> List<Task<Param, Result>>.executeAsync(param: Param): Flow<ResultState<Result>> = channelFlow {


    val listDeferred = sortedByDescending {

        it.priority()
    }.map {

        async {

            val output = it.execute(param)

            if (output.isSuccess()) {

                offerActive(output)
            }

            output
        }
    }

    launch(Dispatchers.IO) {

        val listTranslate = listDeferred.awaitAll()

        if (listTranslate.all { it.isFailed() }) {

            offerActive(listTranslate.first())
        }
    }

    awaitClose {
    }
}