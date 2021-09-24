package com.example.carexplorer.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.PathInterpolator
import androidx.annotation.AnimRes
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import com.example.carexplorer.R
import com.example.carexplorer.helpers.util.hideKeyboard
import com.example.carexplorer.ui.activity.AppActivity
import com.hannesdorfmann.fragmentargs.FragmentArgs
import dagger.android.support.AndroidSupportInjection
import moxy.MvpAppCompatFragment
import timber.log.Timber

abstract class BaseFragment : MvpAppCompatFragment() {
    @get:LayoutRes
    protected abstract val layoutRes: Int

    private var nextAnimation: Int? = null
    val hasAnimation get() = nextAnimation != null

    /**@returns `true` when fragment requires light status bar with dark icons, `null` when it will handle status bar color itself*/
    open val statusBarLightBackground: Boolean? get() = false
    open val transparentStatusBar: Boolean get() = true
    open val isBottomBarVisible: Boolean
        get() = true
    open val isFullScreen: Boolean
        get() = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(layoutRes, container, false).also {
            Timber.v("onCreateView ${javaClass.simpleName}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        FragmentArgs.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        view?.hideKeyboard()
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        Timber.i("OnCreateAnimation")
        val loadedAnimation = if (nextAnimation != null) {
            val anim = nextAnimation!!
            nextAnimation = null
            anim
        } else nextAnim
        if (loadedAnimation == 0) {
            /** case when fragment transition was without animation*/
            if (enter && sharedElementEnterTransition == null && returnTransition == null && reenterTransition == null) {
                onFragmentShownAfterTransition()
            }
            return null
        }

        val nextAnimation = AnimationUtils.loadAnimation(context, loadedAnimation)
        val interpolator = PathInterpolator(1.000f, 0.035f, 0.955f, 0.615f)
        when (loadedAnimation) {
            R.anim.enter_from_right, R.anim.exit_to_right -> {
                val oldBack = view?.background
                if (oldBack == null) {
                    view?.apply {
                        setBackgroundResource(R.color.white)
                    }
                }

                nextAnimation.interpolator = interpolator
                nextAnimation.setAnimationListener(object : Animation.AnimationListener {
                    private var startZ = 0f
                    override fun onAnimationStart(animation: Animation) {
                        view?.apply {
                            startZ = ViewCompat.getTranslationZ(this)
                            ViewCompat.setTranslationZ(this, 1f)
                        }
                        if (loadedAnimation == R.anim.exit_to_right) {
                            onTransitionStartToHideFragment()
                        }
                    }

                    override fun onAnimationEnd(animation: Animation) {
                        // Short delay required to prevent flicker since other Fragment wasn't removed just yet.
                        view?.apply {
                            this.postDelayed({
                                ViewCompat.setTranslationZ(this, startZ)
                                background = oldBack
                            }, 10)
                        }
                        if (loadedAnimation == R.anim.enter_from_right) {
                            onFragmentShownAfterTransition()
                        }
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
                return nextAnimation
            }
            R.anim.exit_to_left -> {
                nextAnimation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationEnd(animation: Animation?) {}
                    override fun onAnimationRepeat(animation: Animation?) {}
                    override fun onAnimationStart(animation: Animation?) {
                        onTransitionStartToHideFragment()
                    }
                })
                return nextAnimation
            }
            R.anim.enter_from_left -> {
                nextAnimation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationEnd(animation: Animation?) {
                        onFragmentShownAfterTransition()
                    }

                    override fun onAnimationRepeat(animation: Animation?) {}
                    override fun onAnimationStart(animation: Animation?) {}
                })
                return nextAnimation
            }
            else -> {
                /** case when fragment transition was without animation*/
                if (enter) {
                    onFragmentShownAfterTransition()
                }
                return null
            }
        }
    }

    open fun onBackPressed() {
        view?.hideKeyboard()
    }

    open fun onTransitionStartToHideFragment() {
        Timber.i("onTransitionStartToHideFragment")
    }

    open fun onFragmentShownAfterTransition() {
        Timber.i("onFragmentShownAfterTransition")
    }

    fun routeToMainTab() {
        (activity as? AppActivity)?.setSelectedBottomBarItem(
            R.id.bottomBarNews,
            true
        )
    }

    fun setNextAnimation(@AnimRes animatorRes: Int?) {
        nextAnimation = animatorRes
    }
}
