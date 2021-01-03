package com.example.carexplorer.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.*
import androidx.core.content.ContextCompat

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this,0)
}

fun View.hideKeyboard() : Boolean {
    try {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken,0)
    } catch (ignored: RuntimeException) {}
    return false
}

fun ViewGroup.inflate(@LayoutRes resId: Int, attachTORoot: Boolean) : View =
    LayoutInflater.from(context).inflate(
        resId,this,attachTORoot
    )

fun View.drawable(@DrawableRes id: Int) = ContextCompat.getDrawable(context, id)

fun Context.quantityString(@PluralsRes id: Int, quantity: Int) =
    resources.getQuantityString(id, quantity, quantity)

fun View.quantityString(@PluralsRes id: Int, quantity: Int) =
    resources.getQuantityString(id, quantity, quantity)

fun View.color(@ColorRes colorRes: Int) = ContextCompat.getColor(context, colorRes)
fun View.string(@StringRes stringRes: Int, vararg format: Any) =
    resources.getString(stringRes, *format)
