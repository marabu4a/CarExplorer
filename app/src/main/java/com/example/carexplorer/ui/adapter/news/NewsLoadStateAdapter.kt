package com.example.carexplorer.ui.adapter.news

import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.carexplorer.R
import com.example.carexplorer.helpers.util.inflate
import com.example.carexplorer.helpers.util.setOnDebouncedClickListener
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_news_error.view.*

class NewsLoadStateAdapter(
    val onRefreshClick: () -> Unit
) : LoadStateAdapter<NewsLoadStateAdapter.BaseLoadStateViewHolder>() {

    override fun getStateViewType(loadState: LoadState): Int = when (loadState) {
        is LoadState.Error -> {
            ERROR
        }
        is LoadState.Loading -> {
            PROGRESS
        }
        is LoadState.NotLoading -> {
            error("Not supported")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): BaseLoadStateViewHolder {
        return when (loadState) {
            LoadState.Loading -> ProgressStateViewHolder(NewsLoadingPlaceholderView(context = parent.context))
            is LoadState.Error -> ErrorStateViewHolder(parent.inflate(R.layout.item_news_error, false)) {
                onRefreshClick()
            }
            is LoadState.NotLoading -> {
                error("Not supported")
            }
        }
    }

    override fun onBindViewHolder(holder: BaseLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    private companion object {
        private const val ERROR = 1
        private const val PROGRESS = 0
    }

    class ProgressStateViewHolder(override val containerView: View) : BaseLoadStateViewHolder(view = containerView) {
        override fun bind(loadState: LoadState) {
            if (loadState is LoadState.Loading) {
                (containerView as ShimmerFrameLayout).startShimmer()
            }
        }
    }

    class ErrorStateViewHolder(
        override val containerView: View,
        val onRefreshClick: () -> Unit
    ) : BaseLoadStateViewHolder(view = containerView) {
        override fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                with(containerView) {
                    errorMessageNews.text = loadState.error.message
                    errorButtonRefreshNews.setOnDebouncedClickListener { onRefreshClick() }
                }
            }
        }
    }

    abstract class BaseLoadStateViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
        abstract fun bind(loadState: LoadState)
    }
}
