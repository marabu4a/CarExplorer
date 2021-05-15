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
import com.example.carexplorer.data.model.enities.News
import com.example.carexplorer.ui.base.BaseAdapter
import com.example.carexplorer.util.setOnDebouncedClickListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_news.view.*
import timber.log.Timber

class NewsAdapter(
    private val onNewsClick: (News) -> Unit,
    private val onFavoriteClick: (News) -> Unit
) : BaseAdapter<NewsAdapter.NewsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder = NewsViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_news,
            parent,
            false
        )
    )

    override fun bind(
        holder: RecyclerView.ViewHolder,
        data: ArrayList<Any>,
        position: Int
    ) = with((holder as NewsViewHolder).containerView) {
        var item = data[position] as News
        Timber.e(item.toString())
        if (item.image.isNotEmpty()) {
            Picasso
                .get()
                .load(item.image)
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

        tvNews.text = item.title
        tvPubDate.text = item.date
        tvNameSource.text = item.sourceName
        if (item.isFavorite) {
            button_favorite_news.isChecked = true
        }
        val scaleAnimation = ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f)
        scaleAnimation.duration = 500
        val bounceInterpolator = BounceInterpolator()
        scaleAnimation.interpolator = bounceInterpolator
        button_favorite_news.setOnCheckedChangeListener(object : View.OnClickListener,
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                p0?.startAnimation(scaleAnimation)
                item = item.copy(isFavorite = p1)
            }

            override fun onClick(p0: View?) {
            }
        })
        button_favorite_news.setOnDebouncedClickListener { onFavoriteClick(item) }
        setOnDebouncedClickListener { onNewsClick(item) }
    }

    class NewsViewHolder(override val containerView: View) : BaseAdapter.BaseViewHolder(containerView)
}

