package com.example.carexplorer.helpers.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import com.example.carexplorer.helpers.navigation.sharedtransition.AnimateArticlePreviewTanstionSet
import com.example.carexplorer.ui.base.BaseFragment
import ru.terrakok.cicerone.commands.Command
import timber.log.Timber

class CustomSupportAppNavigator(
    activity: FragmentActivity,
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) : TaggedFragmentsAppNavigator(activity, fragmentManager, containerId) {

    private var currentCommand: Command? = null

    override fun applyCommand(command: Command?) {
        currentCommand = command
        Timber.d(command.toString())
        super.applyCommand(command)
    }

    override fun onFragmentBack(newFragmentTag: String) {
        super.onFragmentBack(newFragmentTag)
        when (val command = currentCommand) {
            is AnimatedCommand -> {
                getCurrentFragment()?.apply {
                    clearAllAnimations()
                    trySetNextAnimation(command.oldScreenAnimation)
                }
                fragmentManager.findFragmentByTag(newFragmentTag)?.apply {
                    clearAllAnimationsExceptSharedReturn()
                    trySetNextAnimation(command.newScreenAnimation)
                }

            }
            is BackToFirst -> {
                getCurrentFragment()?.clearAllAnimations()
                fragmentManager.findFragmentByTag(newFragmentTag)?.clearAllAnimations()
            }
        }
    }

    override fun setupFragmentTransaction(
        command: Command?,
        currentFragment: Fragment?,
        nextFragment: Fragment?,
        fragmentTransaction: FragmentTransaction?
    ) {
        when (command) {
            is AnimatedCommand -> {
                //(currentFragment as? BaseFragment)?.showForeground()
                currentFragment.trySetNextAnimation(command.oldScreenAnimation)
                nextFragment.trySetNextAnimation(command.newScreenAnimation)
            }
            is SharedElementCommand -> {
                nextFragment?.sharedElementEnterTransition = AnimateArticlePreviewTanstionSet(false).apply {
                    addListener(object : Transition.TransitionListener {
                        override fun onTransitionStart(transition: Transition) {
                            Timber.d("enter sharedElementEnterTransition onTransitionStart")
                        }

                        override fun onTransitionEnd(transition: Transition) {
                            (nextFragment as? BaseFragment)?.onFragmentShownAfterTransition()
                            Timber.d("enter sharedElementEnterTransition onTransitionEnd")
                        }

                        override fun onTransitionCancel(transition: Transition) {
                            Timber.d("enter sharedElementEnterTransition onTransitionCancel")
                        }

                        override fun onTransitionPause(transition: Transition) {
                            Timber.d("enter sharedElementEnterTransition onTransitionPause")
                        }

                        override fun onTransitionResume(transition: Transition) {
                            Timber.d("enter sharedElementEnterTransition onTransitionResume")
                        }
                    })
                }
                command.enterNewFragmentTransition?.let { nextFragment?.enterTransition = it }
                command.returnNewFragmentTransition?.let { nextFragment?.returnTransition = it }
                command.exitCurrentFragmentTransition?.let { currentFragment?.exitTransition = it }
                command.reenterCurrentFragmentTransition?.let { currentFragment?.reenterTransition = it }
                nextFragment?.sharedElementReturnTransition = ChangeBounds().apply {
                    addListener(object : Transition.TransitionListener {
                        override fun onTransitionStart(transition: Transition) {
                            (nextFragment as? BaseFragment)?.onTransitionStartToHideFragment()
                            Timber.d("exit sharedElementEnterTransition onTransitionStart")
                        }

                        override fun onTransitionEnd(transition: Transition) {
                            currentFragment?.exitTransition = null
                            currentFragment?.reenterTransition = null
                            (currentFragment as? BaseFragment)?.onFragmentShownAfterTransition()
                            Timber.d("exit sharedElementEnterTransition onTransitionEnd")
                        }

                        override fun onTransitionCancel(transition: Transition) {
                            Timber.d("exit sharedElementEnterTransition onTransitionCancel")
                        }

                        override fun onTransitionPause(transition: Transition) {
                            Timber.d("exit sharedElementEnterTransition onTransitionPause")
                        }

                        override fun onTransitionResume(transition: Transition) {
                            Timber.d("exit sharedElementEnterTransition onTransitionResume")
                        }
                    })
                }
                command.views.forEach { view ->
                    if (!view.transitionName.isNullOrEmpty()) {
                        fragmentTransaction?.addSharedElement(view, view.transitionName)
                    }
                }
            }
        }
        super.setupFragmentTransaction(command, currentFragment, nextFragment, fragmentTransaction)
    }

    private fun getCurrentFragment(): Fragment? {
        return fragmentManager.findFragmentById(containerId)
    }

    private fun Fragment?.trySetNextAnimation(animationRes: Int?) {
        (this as? BaseFragment)?.setNextAnimation(animationRes)
    }

    private fun Fragment.clearAllAnimations() {
        exitTransition = null
        enterTransition = null
        sharedElementEnterTransition = null
        sharedElementReturnTransition = null
        reenterTransition = null
        returnTransition = null
    }

    private fun Fragment.clearAllAnimationsExceptSharedReturn() {
        exitTransition = null
        enterTransition = null
        sharedElementEnterTransition = null
        reenterTransition = null
        returnTransition = null
    }

}