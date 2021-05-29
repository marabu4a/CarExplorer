package com.example.carexplorer.helpers.util

import android.os.SystemClock
import android.view.MotionEvent
import android.view.View

fun View.setOnDebouncedClickListener(action: () -> Unit) {
    val actionDebouncer = UnitActionDebouncer(action)

    // This is the only place in the project in the project where we should actually use setOnClickListener
    setOnClickListener {
        actionDebouncer.notifyAction()
    }
}

fun View.setOnDebouncedTouchListener(action: (MotionEvent, ActionDebouncer<*, *>) -> Boolean) {
    val actionDebouncer = ActionDebouncer { arg: Pair<MotionEvent, ActionDebouncer<*, *>> ->
        action(arg.first, arg.second)
    }

    setOnTouchListener { _, motionEvent ->
        val actionAllowed = actionDebouncer.notifyAction(Pair(motionEvent, actionDebouncer))
        //whether event is intercepted
        when {
            actionAllowed -> actionDebouncer.result!!
            else -> true
        }
    }
}

fun View.removeOnDebouncedClickListener() {
    setOnClickListener(null)
    isClickable = false
}

open class ActionDebouncer<T, R>(private val action: (T) -> R) {

    companion object {
        const val DEBOUNCE_INTERVAL_MILLISECONDS = 300L
    }

    private var ignoreAction = false
    private var lastActionTime = 0L
    var result: R? = null
        private set

    fun notifyAction(arg: T): Boolean {
        val now = SystemClock.elapsedRealtime()

        val millisecondsPassed = now - lastActionTime
        val actionAllowed = millisecondsPassed > DEBOUNCE_INTERVAL_MILLISECONDS

        if (!actionAllowed) {
            return false
        }
        result = action(arg).let {
            if (ignoreAction) {
                ignoreAction = false
                return false
            }
            it
        }
        lastActionTime = now
        return true
    }

    fun ignoreAction() {
        ignoreAction = true
    }
}

class UnitActionDebouncer(private val action: () -> Unit) :
    ActionDebouncer<Unit, Unit>({ action() }) {
    fun notifyAction(): Boolean {
        return notifyAction(Unit)
    }
}