package com.example.carexplorer.ui.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carexplorer.R
import com.example.carexplorer.data.model.enities.Source
import com.example.carexplorer.ui.base.BaseAdapter
import com.example.carexplorer.util.setOnDebouncedClickListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_source.view.*

class SourcesAdapter(
    private val onSourceClick: (Source) -> Unit
) : BaseAdapter<SourcesAdapter.SourcesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SourcesViewHolder
        = SourcesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_source,parent,false))

    override fun bind(holder: RecyclerView.ViewHolder, data: ArrayList<Any>, position: Int) = with((holder as SourcesViewHolder).containerView) {
        val item = data[position] as Source
        Picasso.get().load(item.image).into(ivImageSource)
        setOnDebouncedClickListener { onSourceClick(item) }
    }

    class SourcesViewHolder(override val containerView: View) : BaseViewHolder(containerView)

}

