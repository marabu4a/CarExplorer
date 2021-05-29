package com.example.carexplorer.ui.adapter.spinner

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.carexplorer.R
import kotlinx.android.synthetic.main.spinner_item_news.view.*

class NewsSpinnerAdapter(
    context: Context
) : ArrayAdapter<NewsSpinnerSelector>(context, 0) {

    val layoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: layoutInflater.inflate(R.layout.spinner_item_news, parent, false)

        getItem(position)?.also {
            setNewsSelectorInfo(view, it)
        }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = if (position == 0) {
            layoutInflater.inflate(R.layout.component_spinner_toolbar, parent, false).apply {
                setOnClickListener {
                    val root = parent.rootView
                    root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
                    root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
                }
            }
        }else {
            layoutInflater.inflate(R.layout.spinner_item_news,parent,false)
        }
        return view
    }

    override fun getCount(): Int = super.getCount() + 1

    override fun isEnabled(position: Int): Boolean = position != 0

    override fun getItem(position: Int): NewsSpinnerSelector? {
        if (position == 0) {
            return null
        }
        return super.getItem(position - 1)
    }

    private fun setNewsSelectorInfo(view: View, newsSpinnerSelector: NewsSpinnerSelector) {
        with(view) {
            newsSpinnerTitle.text = newsSpinnerSelector.type
        }
    }
}