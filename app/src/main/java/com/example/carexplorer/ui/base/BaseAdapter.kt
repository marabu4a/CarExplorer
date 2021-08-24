package com.example.carexplorer.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class BaseAdapter<vh : BaseAdapter.BaseViewHolder> : RecyclerView.Adapter<vh>() {

    var items: ArrayList<Any> = ArrayList()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: vh, position: Int) {
        bind(holder, items, position)
    }

    fun getItem(position: Int): Any {
        return items[position]
    }

    fun refresh(list: List<Any>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun add(listAny: List<Any>) {
        clear()
        items.addAll(listAny)
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
    }

    open fun bind(
        holder: RecyclerView.ViewHolder,
        data: ArrayList<Any>,
        position: Int
    ) {
    }

    abstract class BaseViewHolder(protected val view: View) : LayoutContainer, RecyclerView.ViewHolder(view)
}
