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
import kotlinx.android.synthetic.main.item_preview_article.view.*

class ListArticlesAdapter : BaseAdapter<ListArticlesViewHolder>() {
//    override val layoutRes: Int = R.layout.item_preview_article

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListArticlesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ListArticlesViewHolder(layoutInflater.inflate(R.layout.item_preview_article,parent,false))
    }

}

class ListArticlesViewHolder(view: View) : BaseAdapter.BaseViewHolder(view) {
    init {
        view.setOnClickListener {
            onClick?.onClick(item,it)
        }

       view.button_favorite_entry.setOnClickListener {
           onClick?.onClick(item,it)
        }

        view.setOnLongClickListener {
            onClick?.onLongClick(item,it)
            true
        }
    }

    override fun onBind(item: Any) {
        (item as CachedArticle).let {
            view.tvNameArticle.text = it.title
            Picasso.get().load(it.image).into(view.ivImageArticle,object : Callback {
                override fun onSuccess() {
                    view.cpvListArticles.visibility = View.GONE
                }

                override fun onError(e: Exception?) {

                }

            })

            val scaleAnimation = ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f)
            scaleAnimation.duration = 500
            val bounceInterpolator = BounceInterpolator()
            scaleAnimation.interpolator = bounceInterpolator
            if (it.cached) {
                view.button_favorite_entry.isChecked = true
            }
            view.button_favorite_entry.setOnCheckedChangeListener(object:View.OnClickListener, CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                    p0?.startAnimation(scaleAnimation);
                }
                override fun onClick(p0: View?) {
                }
            })
        }
    }
}