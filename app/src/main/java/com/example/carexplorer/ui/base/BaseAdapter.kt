package com.example.carexplorer.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<vh : BaseAdapter.BaseViewHolder> : RecyclerView.Adapter<vh>() {
//    abstract val layoutRes : Int

    var items : ArrayList<Any> = ArrayList()
    var onClick: OnClick? = null

    fun refreshData(list : List<Any>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

//
//    abstract fun createHolder(view: View,viewType: Int) : vh
//
    override fun getItemCount(): Int {
        return items.size
    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vh {
//            val inflater = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
//            return createHolder(inflater, viewType)
//        }


    override fun onBindViewHolder(holder: vh, position: Int) {
            holder.bind(items[position])
            holder.onClick = onClick
    }

    fun getItem(position: Int) : Any {
        return items[position]
   }

   fun add(item : Any) {
       items.add(item)
   }

   fun add(listAny : List<Any>) {
       items.addAll(listAny)
  }

   fun clear() {
       items.clear()
   }





    fun setOnClick(click : (Any?,View) -> Unit,longClick : (Any?,View) -> Unit = {_,_ ->}) {
        onClick = object : OnClick {
            override fun onClick(item: Any?, view: View) {
                click(item,view)
            }

            override fun onLongClick(item: Any?, view: View) {
                longClick(item,view)
            }

        }

    }
    interface OnClick {
        fun onClick(item: Any?,view: View)
        fun onLongClick(item: Any?,view: View)
    }
    abstract class BaseViewHolder(protected val view: View) : RecyclerView.ViewHolder(view) {
        abstract fun onBind(item : Any)

        var onClick : OnClick? = null
        var item : Any? = null

        init {
            view.setOnClickListener {
                onClick?.onClick(item,it)
            }
            view.setOnLongClickListener {
                onClick?.onLongClick(item,it)
                false
            }
        }
        fun bind(item: Any) {
            this.item = item

            onBind(item)
        }
    }
}
