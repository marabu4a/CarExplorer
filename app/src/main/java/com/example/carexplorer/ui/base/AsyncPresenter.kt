package com.example.carexplorer.ui.base

import com.example.carexplorer.helpers.flow.FlowExecutorFactory
import com.example.carexplorer.helpers.flow.UseCaseExecutorsFactoryProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import moxy.MvpView
import org.xml.sax.ErrorHandler

@UseExperimental(ExperimentalCoroutinesApi::class, InternalCoroutinesApi::class)
open class AsyncPresenter<TView : MvpView>(
    factoryProvider: UseCaseExecutorsFactoryProvider,
    errorHandler: ErrorHandler
) : BasePresenter<TView>(factoryProvider, errorHandler), FlowExecutorFactory {

    protected val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    protected fun <TResult> (suspend () -> TResult).launchOnMain(onObtain: (TResult) -> Unit = {}) {
        scope.launch {
            try {
                onObtain(invoke())
            } catch (e: CancellationException) {
                // scope was cancelled in onDestroy
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    /**Handles error in flow but also propagates it downstream for retry() to work.
     * todo: Think about including retry() directly to AsyncPresenter subscribe() in 1.4.0*/
    @ExperimentalCoroutinesApi
    protected fun <T> Flow<T>.onError(action: suspend FlowCollector<T>.(Throwable) -> Unit) =
        catch {
            action.invoke(this, it)
            throw it
        }

    protected fun <T> Flow<T>.subscribe(action: suspend (T) -> Unit): Job {
        return scope.launch {
            try {
                collect(action)
            } catch (e: CancellationException) {
                // scope was cancelled in onDestroy
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    /**Lazy subscription opens flow but doesn't start collecting it until [LazySubscription.run] is called.
     * Useful for subscribing to broadcasts and avoiding lambda-variables. */
    protected fun <T> Flow<T>.lazySubscribe(action: suspend (T) -> Unit): LazySubscription<T> {
        return LazySubscription(this, action)
    }

    protected inner class LazySubscription<T> constructor(
        private val flow: Flow<T>,
        private val action: suspend (T) -> Unit
    ) {
        fun run(): Job {
            return scope.launch {
                try {
                    flow.collect(action)
                } catch (e: CancellationException) {
                    // scope was cancelled in onDestroy
                } catch (e: Exception) {
                    onError(e)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}