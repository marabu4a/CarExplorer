package com.example.carexplorer.ui.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.carexplorer.R
import com.example.carexplorer.data.model.enities.Source
import com.example.carexplorer.ui.base.BaseAdapter
import com.example.carexplorer.ui.base.BaseAdapter.BaseViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_source.view.*

class SourcesAdapter : BaseAdapter<SourcesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SourcesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SourcesViewHolder(layoutInflater.inflate(R.layout.item_source,parent,false))
    }
}
    class SourcesViewHolder(view : View) : BaseViewHolder(view) {
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
            (item as Source).let {
                Picasso.get().load(it.image).into(view.ivImageSource)

            }
        }
    }
