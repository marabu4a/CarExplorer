package com.example.carexplorer.helpers

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.dueeeke.tablayout.SegmentTabLayout

class SegmentTabBehavior : CoordinatorLayout.Behavior<SegmentTabLayout>() {

    private var height : Int = 0

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: SegmentTabLayout,
        layoutDirection: Int
    ): Boolean {
        height = child.height
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: SegmentTabLayout,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: SegmentTabLayout,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (dyConsumed > 0) {
            slideDown(child)
        }
        else if (dyConsumed < 0) {
            slideUp(child)
        }
    }

    private fun slideUp(child : SegmentTabLayout) {
        child.clearAnimation()
        child.animate().translationY(0f)
    }

    private fun slideDown(child: SegmentTabLayout) {
        child.clearAnimation()
        child.animate().translationY(height.toFloat() + height.toFloat())
    }

}