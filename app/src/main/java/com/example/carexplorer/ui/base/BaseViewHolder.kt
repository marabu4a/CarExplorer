package com.example.carexplorer.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class BaseViewHolder(protected val view: View) : LayoutContainer, RecyclerView.ViewHolder(view)