package com.example.carexplorer.helpers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.example.carexplorer.R
import kotlinx.android.synthetic.main.fragment_sheet_filter.*

class BottomSheetFilter(val bottomSheetCallback : CallbackFilter) : SuperBottomSheetFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_sheet_filter,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        radioGroupFilter.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when (checkedId) {
                    R.id.rb1 -> {
                        bottomSheetCallback.filter(3)
                    }
                    R.id.rb2 -> {
                        bottomSheetCallback.filter(0)
                    }
                    else -> {
                        bottomSheetCallback.filter(1)
                    }
                }
            }
        })
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getCornerRadius(): Float = context!!.resources.getDimension(R.dimen.bottom_sheet_sheet_corner_radius)


    interface CallbackFilter {
        fun filter(tab : Int)
    }
}