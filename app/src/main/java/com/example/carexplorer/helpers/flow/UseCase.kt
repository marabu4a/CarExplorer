package com.example.carexplorer.helpers.flow

import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

interface UseCase<in TParams, out TResult> {

    suspend fun execute(params: TParams): TResult
}

suspend fun <TResult> UseCase<Unit, TResult>.execute() = execute(Unit)

class UseCaseExecutor<in TParams, TResult>(
    private val workContext: CoroutineContext,
    private val useCase: UseCase<TParams, TResult>,
    private val onError: (Throwable) -> Unit,
    private val onObtain: suspend (TParams, Deferred<TResult>) -> Unit,
    private val scope: CoroutineScope
) {

    private var job: Job? = null
    fun execute(params: TParams, cancelPrevious: Boolean = true) {
        if (cancelPrevious) {
            cancel()
        }
        job = scope.launch {
            try {
                val deferred = coroutineScope {
                    async(workContext) {
                        useCase.execute(params)
                    }
                }
                onObtain(params, deferred)
                deferred.await()
            } catch (e: CancellationException) {
                Timber.i("$useCase cancelled")
            } catch (e: Throwable) {
                onError(e)
            }
        }
    }

    fun cancel() {
        job?.cancelChildren()
        job?.cancel()
        job = null
    }

    fun isExecuted() = job?.isActive == true
}

fun <TResult> UseCaseExecutor<Unit, TResult>.execute(cancelPrevious: Boolean = true) =
    execute(Unit, cancelPrevious)

interface ExecutorsFactory {
    fun <TParams, TResult> create(
        useCase: UseCase<TParams, TResult>,
        onError: (Throwable) -> Unit,
        onObtain: suspend (TParams, Deferred<TResult>) -> Unit,
        workContext: CoroutineContext = Dispatchers.IO
    ): UseCaseExecutor<TParams, TResult>

    fun <TParams, TResult> create(
        useCase: UseCase<TParams, TResult>,
        onObtain: suspend (TParams, Deferred<TResult>) -> Unit
    ): UseCaseExecutor<TParams, TResult>

    fun cancelAll()
}

class UseCaseExecutorsFactory(
    private val defaultOnError: (Throwable) -> Unit
) : ExecutorsFactory {
    private val job = SupervisorJob()
    val scope = CoroutineScope(Dispatchers.Main + job)

    override fun <TParams, TResult> create(
        useCase: UseCase<TParams, TResult>,
        onError: (Throwable) -> Unit,
        onObtain: suspend (TParams, Deferred<TResult>) -> Unit,
        workContext: CoroutineContext
    ) = UseCaseExecutor(
        workContext,
        useCase,
        onError,
        onObtain,
        scope
    )

    override fun <TParams, TResult> create(
        useCase: UseCase<TParams, TResult>,
        onObtain: suspend (TParams, Deferred<TResult>) -> Unit
    ) = create(
        useCase,
        defaultOnError,
        onObtain,
        Dispatchers.IO
    )

    override fun cancelAll() {
        scope.coroutineContext.cancelChildren()
    }
}

object UseCaseExecutorsFactoryProvider {
    fun create(onError: (Throwable) -> Unit) = UseCaseExecutorsFactory(onError)
}
