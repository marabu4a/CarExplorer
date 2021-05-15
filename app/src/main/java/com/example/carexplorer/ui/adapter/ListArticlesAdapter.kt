package com.example.carexplorer.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.example.carexplorer.R
import com.example.carexplorer.data.model.enities.Article
import com.example.carexplorer.ui.base.BaseAdapter
import com.example.carexplorer.util.setOnDebouncedClickListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_preview_article.view.*

class ListArticlesAdapter(
    private val onArticleClick: (Article) -> Unit,
    private val onFavoriteClick: (Article) -> Unit
) : BaseAdapter<ListArticlesAdapter.ListArticlesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListArticlesViewHolder
        = ListArticlesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_preview_article, parent, false))


    override fun bind(
        holder: RecyclerView.ViewHolder,
        data: ArrayList<Any>,
        position: Int
    ) = with((holder as ListArticlesViewHolder).containerView) {
        var item = data[position] as Article
        setOnDebouncedClickListener { onArticleClick(item) }
        tvNameArticle.text = item.title
        Picasso.get().load(item.image).into(ivImageArticle, object : Callback {
            override fun onSuccess() { cpvListArticles.visibility = View.GONE }
            override fun onError(e: Exception?) {}
        })
        val scaleAnimation = ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f)
        scaleAnimation.duration = 500
        val bounceInterpolator = BounceInterpolator()
        scaleAnimation.interpolator = bounceInterpolator
        if (item.isFavorite) {
            button_favorite_entry.isChecked = true
        }
        button_favorite_entry.setOnCheckedChangeListener(object : View.OnClickListener,
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                p0?.startAnimation(scaleAnimation);
                item = item.copy(isFavorite = p1)
            }

            override fun onClick(p0: View?) {
            }
        })
        button_favorite_entry.setOnDebouncedClickListener {
            onFavoriteClick(item)
        }
    }

    class ListArticlesViewHolder(override val containerView: View) : BaseAdapter.BaseViewHolder(containerView)
}

