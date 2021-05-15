package com.example.carexplorer.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carexplorer.R
import com.example.carexplorer.data.model.enities.Category
import com.example.carexplorer.ui.base.BaseAdapter
import com.example.carexplorer.util.setOnDebouncedClickListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_category.view.*

class CategoriesAdapter(
    private val onCategoryClick: (Category) -> Unit
) : BaseAdapter<CategoriesAdapter.CategoriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder
        = CategoriesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category,parent,false))

    override fun bind(holder: RecyclerView.ViewHolder, data: ArrayList<Any>, position: Int) = with((holder as CategoriesViewHolder).containerView) {
        val item = data[position] as Category
        cvCategory.setOnDebouncedClickListener { onCategoryClick(item) }
        tvNameCategory.text = item.name
        tvInfoCategory.text = item.description
        Picasso.get().load(item.image).into(ivImageCategory)
    }

    class CategoriesViewHolder(override val containerView: View) : BaseViewHolder(containerView)

}