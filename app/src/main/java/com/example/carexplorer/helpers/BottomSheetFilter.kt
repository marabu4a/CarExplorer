package com.example.carexplorer.helpers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.example.carexplorer.R
import kotlinx.android.synthetic.main.fragment_sheet_filter.*

class BottomSheetFilter(
    private val firstValuePair: Pair<String,Boolean>,
    private val secondValuePair: Pair<String,Boolean>,
    val OnSelected: (String) -> Unit) : SuperBottomSheetFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_sheet_filter,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(firstValuePair) {
            bottomSheetFirstRB.text = first
            bottomSheetFirstRB.isChecked = second
        }
        with(secondValuePair) {
            bottomSheetSecondRB.text = first
            bottomSheetSecondRB.isChecked = second
        }
        radioGroupFilter.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.bottomSheetFirstRB -> {
                    OnSelected(firstValuePair.first)
                }
                else -> {
                    OnSelected(secondValuePair.first)
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getCornerRadius(): Float = requireContext().resources.getDimension(R.dimen.bottom_sheet_sheet_corner_radius)

}