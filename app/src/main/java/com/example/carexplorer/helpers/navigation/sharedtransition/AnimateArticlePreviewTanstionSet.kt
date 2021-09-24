package com.example.carexplorer.helpers.navigation.sharedtransition

import android.widget.ImageView
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.ChangeTransform
import androidx.transition.TransitionSet

data class AnimateArticlePreviewTanstionSet(
    val isBack: Boolean,
    val isStartCollapsing: Boolean = true
) : TransitionSet() {
    init {
        ordering = ORDERING_TOGETHER
        addTransition(ChangeBounds())
        addTransition(ChangeTransform())
        addTransition(ChangeImageTransform())
        addTarget(ImageView::class.java)
    }
}