package com.example.carexplorer.helpers.util

import android.os.Bundle
import android.os.Parcelable
import com.hannesdorfmann.fragmentargs.bundler.ArgsBundler

class ParcelableArgsBundler : ArgsBundler<Parcelable> {

    override fun put(key: String?, value: Parcelable?, bundle: Bundle) {
        bundle.putParcelable(key,value)
    }

    override fun <V : Parcelable?> get(key: String?, bundle: Bundle): V? {
        return bundle.getParcelable(key)
    }
}

class StringListArgsBundler : ArgsBundler<List<String>> {
    override fun put(key: String?, value: List<String>?, bundle: Bundle?) {
        bundle?.putStringArray(key, value?.toTypedArray())
    }

    override fun <V : List<String>?> get(key: String?, bundle: Bundle?): V =
        bundle?.getStringArray(key)?.toList() as V
}