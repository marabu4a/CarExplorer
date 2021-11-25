package com.example.carexplorer.ui.adapter.news

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import com.example.carexplorer.R
import com.example.carexplorer.data.model.enities.News
import com.example.carexplorer.helpers.setImageUrl
import com.example.carexplorer.helpers.util.inflate
import com.example.carexplorer.helpers.util.setOnDebouncedClickListener
import com.example.carexplorer.ui.adapter.NewsAdapter.NewsViewHolder
import com.example.carexplorer.ui.base.BasePagingAdapter
import com.example.carexplorer.ui.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_news.view.*

class NewsPagingAdapter(
    private val onNewsClick: (News) -> Unit,
    private val onFavoriteClick: (News) -> Unit
) : BasePagingAdapter<News, NewsViewHolder>(REPO_COMPARATOR) {

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<News>() {

            override fun areItemsTheSame(oldItem: News, newItem: News): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: News, newItem: News): Boolean =
                oldItem == newItem
        }
    }

    override val layoutRes: Int
        get() = R.layout.item_news

    override fun bind(holder: BaseViewHolder, item: News?, position: Int) {
        with((holder as NewsViewHolder).containerView) {
            item?.let {
                if (item.image.isNotEmpty()) {
                    ivNews.setImageUrl(
                        item.image
                    )
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
                        //item = item.copy(isFavorite = p1)
                    }

                    override fun onClick(p0: View?) {
                    }
                })
                button_favorite_news.setOnDebouncedClickListener { onFavoriteClick(item) }
                setOnDebouncedClickListener { onNewsClick(item) }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder =
        NewsViewHolder(parent.inflate(layoutRes, false))

}