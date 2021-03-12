package com.example.carexplorer.helpers.flow

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

typealias FlowBuilder<TResult> = Flow<Result<TResult>>.() -> Flow<Result<TResult>>
typealias ExecutionFlowMaker<TParams, TResult> = (Channel<TParams>) -> Flow<Result<TResult>>

class FlowExecutor<in TParams, TResult>(
    private val flowMaker: ExecutionFlowMaker<TParams, TResult>,
    private val channelCapacity: Int = Channel.BUFFERED
) {
    private var paramsChannel: Channel<TParams>? = null

    fun executionFlow(): Flow<Result<TResult>> {
        paramsChannel?.close()
        return flowMaker(
            Channel<TParams>(channelCapacity).also { paramsChannel = it }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
            /**@return if executor is ready to accept params and [queueExecution] will not lead to an error*/
    fun isReady(): Boolean = paramsChannel?.isClosedForSend == false

    /**Add specific task parameters to execution queue. Tasks will be performed one by one after [executionFlow]
     * is subscribed upon and results will be passed via that flow.*/
    fun queueExecution(params: TParams) {
        paramsChannel?.offer(params)
    }

    /**If pending operation is executed, awaits its result, passes in to the flow and then completes flow.
     * If next [queueExecution] is awaited, completes flow immediately.*/
    fun closeOnResult() {
        paramsChannel?.close()
    }
}

@UseExperimental(ExperimentalCoroutinesApi::class)
interface FlowExecutorFactory {
    fun <TParam, TResult> (suspend (TParam) -> TResult).asFlowExecutor(
        builder: FlowBuilder<TResult> = { this }
    ): FlowExecutor<TParam, TResult> {
        val source = this
        return FlowExecutor({ params: Channel<TParam> ->
            builder(
                flow {
                    for (p in params) {
                        emit(
                            try {
                                Result.success(source(p))
                            } catch (e: Throwable) {
                                Result.failure<TResult>(e)
                            }
                        )
                    }
                }
            )
        })
    }

    fun <TParam, TResult> (UseCase<TParam, TResult>).asFlowExecutor(
        builder: FlowBuilder<TResult> = { this }
    ): FlowExecutor<TParam, TResult> {
        return ::execute.asFlowExecutor(builder)
    }

    fun <TParam, TResult> ((TParam) -> Flow<TResult>).makeFlowExecutor(
        builder: FlowBuilder<TResult> = { this }
    ): FlowExecutor<TParam, TResult> {
        val flowSource = this
        return FlowExecutor({ params: Channel<TParam> ->
            builder(
                flow {
                    for (p in params) {
                        flowSource(p).catch {
                            emit(Result.failure<TResult>(it))
                        }.collect {
                            emit(Result.success(it))
                        }
                    }
                }
            )
        })
    }

    fun <TResult> (() -> Flow<TResult>).makeFlowExecutor(
        builder: FlowBuilder<TResult> = { this }
    ): FlowExecutor<Unit, TResult> {
        return { u: Unit -> this() }.makeFlowExecutor(builder)
    }

    //todo: include only to builder scope...
    fun <T> Flow<Result<T>>.onSuccess(action: (T) -> Unit) = onEach {
        it.getOrNull()?.let(action)
    }

    fun <T> Flow<Result<T>>.onFailure(action: (Throwable) -> Unit) = onEach {
        it.exceptionOrNull()?.let(action)
    }

    fun <T> Flow<Result<T>>.unwrap(): Flow<T> = map {
        if (it.isSuccess) it.getOrNull()!!
        else throw it.exceptionOrNull()!!
    }

    fun FlowExecutor<Unit, *>.queueExecution() = queueExecution(Unit)
}