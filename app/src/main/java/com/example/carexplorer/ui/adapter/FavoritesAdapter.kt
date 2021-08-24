package com.example.carexplorer.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carexplorer.R
import com.example.carexplorer.data.model.enities.Article
import com.example.carexplorer.data.model.enities.Favorite
import com.example.carexplorer.data.model.enities.News
import com.example.carexplorer.helpers.util.setOnDebouncedClickListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_news.view.*
import kotlinx.android.synthetic.main.item_preview_article.view.*

class FavoritesAdapter(
    private val onItemClick: (Favorite) -> Unit,
    private val onRemoveClick: (Favorite) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val favorites: ArrayList<Favorite> = arrayListOf()

    companion object {
        private const val TYPE_NEWS = 0
        private const val TYPE_ARTICLES = 1
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = favorites[position]
        when (holder) {
            is FavoritesNewsVH -> {
                (item as News).also {
                    with(holder.containerView) {
                        if (it.image.isNotEmpty()) {
                            Picasso
                                .get()
                                .load(it.image)
                                .error(R.drawable.placeholder)
                                .into(ivNews, object : Callback {
                                    override fun onSuccess() {
                                        cpv.visibility = View.GONE
                                    }

                                    override fun onError(e: Exception?) {
                                        Picasso.get().load(R.drawable.placeholder).into(ivNews)
                                        cpv.visibility = View.GONE
                                    }

                                })
                        }
                        tvNews.text = it.title
                        tvPubDate.text = it.date
                        tvNameSource.text = it.sourceName
                        button_favorite_news.isChecked = true
                        button_favorite_news.setOnDebouncedClickListener {
                            onRemoveClick(item)
                        }
                        setOnDebouncedClickListener { onItemClick(item) }
                    }
                }
            }
            is FavoritesArticleVH -> {
                (item as Article).also {
                    with(holder.containerView) {
                        tvNameArticle.text = it.title
                        Picasso.get().load(it.image).into(ivImageArticle, object : Callback {
                            override fun onSuccess() { cpvListArticles.visibility = View.GONE }
                            override fun onError(e: Exception?) {}
                        })
                        setOnDebouncedClickListener { onItemClick(item) }
                        button_favorite_entry.isChecked = true

                        button_favorite_entry.setOnDebouncedClickListener {
                            onRemoveClick(item)
                        }
                    }
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_NEWS -> {
                FavoritesNewsVH(LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false))
            }
            TYPE_ARTICLES -> {
                FavoritesArticleVH(LayoutInflater.from(parent.context).inflate(R.layout.item_preview_article, parent, false))
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (favorites[position]) {
            is News -> 0
            else -> 1
        }
    }

    fun clear() {
        favorites.clear()
    }

    fun addAll(items: List<Favorite>) {
        favorites.clear()
        favorites.addAll(items)
        notifyDataSetChanged()
    }

    class FavoritesNewsVH(override val containerView: View) : LayoutContainer, RecyclerView.ViewHolder(containerView)
    class FavoritesArticleVH(override val containerView: View) : LayoutContainer, RecyclerView.ViewHolder(containerView)
}
