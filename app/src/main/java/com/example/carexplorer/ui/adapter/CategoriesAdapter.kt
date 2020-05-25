package com.example.carexplorer.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.carexplorer.R
import com.example.carexplorer.data.model.Category
import com.example.carexplorer.ui.base.BaseAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_category.view.*

class CategoriesAdapter : BaseAdapter<CategoriesAdapter.CategoriesViewHolder>() {
//    override val layoutRes: Int = R.layout.item_category

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CategoriesViewHolder(layoutInflater.inflate(R.layout.item_category,parent,false))
    }


    class CategoriesViewHolder(view: View) : BaseViewHolder(view) {

        init {
            view.setOnClickListener {
                onClick?.onClick(item,it)
            }

            view.setOnLongClickListener {
                onClick?.onLongClick(item,it)
                true
            }
        }

        override fun onBind(item: Any) {
            (item as Category).let {
                view.tvNameCategory.text = it.name
                view.tvInfoCategory.text = it.info
                Picasso.get().load(it.image).into(view.ivImageCategory)
            }
        }
    }


}