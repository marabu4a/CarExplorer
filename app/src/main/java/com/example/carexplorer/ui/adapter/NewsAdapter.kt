package com.example.carexplorer.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.CompoundButton
import com.example.carexplorer.R
import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.ui.base.BaseAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_news.view.*

class NewsAdapter : BaseAdapter<NewsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return NewsViewHolder(
            layoutInflater.inflate(
                R.layout.item_news,
                parent,
                false
            )
        )
    }

}

class NewsViewHolder(view: View) : BaseAdapter.BaseViewHolder(view) {
    init {
        view.setOnClickListener {
            onClick?.onClick(item,it)
        }
        view.button_favorite_news.setOnClickListener {
            onClick?.onClick(item,it)
        }
        view.setOnLongClickListener {
            onClick?.onLongClick(item, it)
            true
        }
    }
    override fun onBind(item: Any) {
        (item as CachedArticle).let {

            Picasso
                .get()
                .load(it.image)
                .error(R.drawable.placeholder)
                .into(view.ivNews, object : Callback {
                    override fun onSuccess() {
                        view.cpv.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        Picasso.get().load(R.drawable.placeholder).into(view.ivNews)
                        view.cpv.visibility = View.GONE
                    }

                })
            //Log.e("Activity",it.image)
            view.tvNews.text = it.title
            view.tvPubDate.text = it.pubDate
            view.tvNameSource.text = it.source
            if (it.cached) {
                view.button_favorite_news.isChecked = true
            }
            val scaleAnimation = ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f)
            scaleAnimation.duration = 500
            val bounceInterpolator = BounceInterpolator()
            scaleAnimation.interpolator = bounceInterpolator

            view.button_favorite_news.setOnCheckedChangeListener(object:View.OnClickListener, CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                    p0?.startAnimation(scaleAnimation)
                }

                override fun onClick(p0: View?) {
                }
            })
        }
    }

}