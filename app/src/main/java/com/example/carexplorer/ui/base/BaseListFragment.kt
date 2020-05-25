package com.example.carexplorer.ui.base
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carexplorer.R

abstract class BaseListFragment : BaseFragment() {
    override val layoutId: Int = R.layout.fragment_list
    lateinit var rView : RecyclerView
    protected lateinit var lManager : RecyclerView.LayoutManager

    protected abstract val viewAdapter : BaseAdapter<*>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lManager = LinearLayoutManager(context)
        rView = view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = lManager
            adapter = viewAdapter
            setHasFixedSize(true)
        }

    }




    override fun onResume() {
        super.onResume()

        base {
            if (showToolbar) supportActionBar?.show() else supportActionBar?.hide()
            supportActionBar?.title = titleToolbar


        }
    }



    fun setOnItemClickListener(func: (Any?,View) -> Unit) {
        viewAdapter.setOnClick(func)
    }


    fun setOnItemLongClickListener(func: (Any?,View) -> Unit) {
        viewAdapter.setOnClick({_,_->},longClick = func)
    }



}