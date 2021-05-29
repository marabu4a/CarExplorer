package com.example.carexplorer.helpers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.example.carexplorer.R
import kotlinx.android.synthetic.main.fragment_bottom_sheet_favorites.*

class FavoritesBottomSheet(private val firstValuePair: Pair<String,Boolean>,
private val secondValuePair: Pair<String,Boolean>,
    private val thirdValuePair: Pair<String,Boolean>,
val OnSelected: (String) -> Unit) : SuperBottomSheetFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_bottom_sheet_favorites,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(firstValuePair) {
            favoritesBottomSheetFirstRB.text = first
            favoritesBottomSheetFirstRB.isChecked = second
        }
        with(secondValuePair) {
            favoritesBottomSheetSecondRB.text = first
            favoritesBottomSheetSecondRB.isChecked = second
        }
        with(thirdValuePair) {
            favoritesBottomSheetThirdRB.text = first
            favoritesBottomSheetThirdRB.isChecked = second
        }
        favoritesRadioGroupFilter.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.favoritesBottomSheetFirstRB -> {
                    OnSelected(firstValuePair.first)
                }
                R.id.favoritesBottomSheetSecondRB -> {
                    OnSelected(secondValuePair.first)
                }
                else -> {
                    OnSelected(thirdValuePair.first)
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getCornerRadius(): Float = requireContext().resources.getDimension(R.dimen.bottom_sheet_sheet_corner_radius)

}