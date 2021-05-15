package com.example.carexplorer.ui.adapter

//class FavoritesAdapter : BaseAdapter<BaseAdapter.BaseViewHolder>(){
//    private val list : ArrayList<CachedArticle> = arrayListOf()
//
//    companion object {
//        private const val TYPE_NEWS = 0
//        private const val TYPE_ENTRY = 1
//    }
//
//
//
//    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
//        val element = items[position]
//        when (holder) {
//            //is NewsViewHolder -> {
//            //    holder.itemView.button_favorite_news.visibility = View.GONE
//            //    holder.bind(element)
//            //    holder.onClick = onClick
//            //}
//            //is ListArticlesViewHolder -> {
//            //    holder.itemView.button_favorite_entry.visibility = View.GONE
//            //    holder.bind(element)
//            //    holder.onClick = onClick
//            //}
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : BaseViewHolder {
//        return when (viewType) {
//            TYPE_NEWS -> {
//                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false)
//               // NewsViewHolder(view)
//            }
//            TYPE_ENTRY -> {
//                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_preview_article,parent,false)
//               // ListArticlesViewHolder(view)
//            }
//            else -> throw IllegalArgumentException("Invalid view type")
//        }
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        (items[position] as CachedArticle).let {
//            return when (it.type) {
//                "news" ->  TYPE_NEWS
//                "entry" -> TYPE_ENTRY
//                else -> throw IllegalArgumentException("Invalid type of data " + position)
//            }
//        }
//
//    }
//
//    fun removeArticle(title : String) : ArrayList<CachedArticle> {
//        var isDifferent = true
//        var i = 0
//                while(isDifferent && i <= items.size) {
//                    (items[i] as CachedArticle).let {
//                        if (it.title == title) {
//                            removeAt(i)
//                            isDifferent = false
//                        }
//                }
//                    i++
//       }
//        return items as ArrayList<CachedArticle>
//
//    }
//
//    private fun removeAt(position: Int)  {
//        (items as ArrayList<CachedArticle>).let {
//            it.removeAt(position)
//        }
//        //items.remove(position)
//        notifyItemRemoved(position)
//        notifyItemRangeChanged(position,items.size)
//    }
//}