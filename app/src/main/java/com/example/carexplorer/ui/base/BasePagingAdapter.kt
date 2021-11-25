package com.example.carexplorer.ui.base

import androidx.annotation.LayoutRes
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

abstract class BasePagingAdapter<T : Any, H : BaseViewHolder>(
    pagingComparator: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, H>(pagingComparator) {

    @get:LayoutRes
    abstract val layoutRes: Int

    abstract fun bind(
        holder: BaseViewHolder,
        item: T?,
        position: Int
    )

    override fun onBindViewHolder(holder: H, position: Int) {
        bind(
            holder = holder,
            item = getItem(position),
            position = position
        )
    }

}