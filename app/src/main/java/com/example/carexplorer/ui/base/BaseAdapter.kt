package com.example.carexplorer.ui.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<vh : BaseViewHolder> : RecyclerView.Adapter<vh>() {

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

}
