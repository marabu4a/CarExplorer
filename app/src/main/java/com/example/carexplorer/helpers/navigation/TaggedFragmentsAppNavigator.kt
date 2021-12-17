package com.example.carexplorer.helpers.navigation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.*

/**
 * Navigator implementation for launch fragments and activities.<br></br>
 * Feature [BackTo] works only for fragments.<br></br>
 * Recommendation: most useful for Single-Activity application.
 */
open class TaggedFragmentsAppNavigator(
    activity: FragmentActivity,
    fragmentManager: FragmentManager,
    containerId: Int
) :
    Navigator {
    private val activity: Activity
    private val fragmentManager: FragmentManager
    private val containerId: Int
    private var localStackCopy: MutableList<ScreenData> = mutableListOf()

    constructor(activity: FragmentActivity, containerId: Int) : this(
        activity,
        activity.supportFragmentManager,
        containerId
    )

    init {
        this.activity = activity
        this.fragmentManager = fragmentManager
        this.containerId = containerId
    }

    override fun applyCommands(commands: Array<Command>) {
        fragmentManager.executePendingTransactions()
        copyStackToLocal()
        // copy stack before apply commands
        for (command in commands) {
            applyCommand(command)
        }
    }

    private fun copyStackToLocal() {
        localStackCopy = mutableListOf()
        val stackSize = fragmentManager.backStackEntryCount
        for (i in 0 until stackSize) {
            fragmentManager.getBackStackEntryAt(i).name?.let { screenObjectName ->
                localStackCopy.add(fromScreenKeyAndCount(screenObjectName))
            }
        }
    }

    /**
     * Perform transition described by the navigation command
     *
     * @param command the navigation command to apply
     */
    protected open fun applyCommand(command: Command?) {
        when (command) {
            is Forward -> activityForward(command)
            is Replace -> activityReplace(command)
            is BackTo -> backTo(command)
            is Back -> fragmentBack()
            is CloseChain -> closeChain(command)
            is OpenChain -> openChain(command)
            is BackToFirst -> {
                localStackCopy.firstOrNull()?.let {
                    backToScreen(screenName = it.screenKey)
                }
            }
        }
    }

    protected fun activityForward(command: Forward) {
        val screen = command.screen as SupportAppScreen
        val activityIntent = screen.getActivityIntent(activity)
        // Start activity
        if (activityIntent != null) {
            val options = createStartActivityOptions(command, activityIntent)
            checkAndStartActivity(screen, activityIntent, options)
        } else {
            fragmentForward(command)
        }
    }

    protected fun fragmentForward(command: Forward) {
        val screen = command.screen as SupportAppScreen
        val screenData = generateScreenData(screen.screenKey, localStackCopy)
        val fragment = createFragment(screen)
        val fragmentTransaction = fragmentManager.beginTransaction()
        setupFragmentTransaction(
            command,
            fragmentManager.findFragmentById(containerId),
            fragment,
            fragmentTransaction
        )
        fragmentTransaction
            .replace(containerId, fragment, screenData.screenKeyAndCount)
            .addToBackStack(screenData.screenKeyAndCount)
            .commit()
        localStackCopy.add(screenData)
    }

    protected open fun fragmentBack() {
        if (localStackCopy.size > 0) {
            fragmentManager.popBackStack()
            localStackCopy.removeAt(localStackCopy.size - 1)
            localStackCopy.lastOrNull()?.let { onFragmentBack(it.screenKeyAndCount) }
        } else {
            activityBack()
        }
    }

    protected open fun onFragmentBack(newFragmentTag: String) {
    }

    protected fun activityBack() {
        activity.finish()
    }

    protected fun activityReplace(command: Replace) {
        val screen = command.screen as SupportAppScreen
        val activityIntent = screen.getActivityIntent(activity)
        // Replace activity
        if (activityIntent != null) {
            val options = createStartActivityOptions(command, activityIntent)
            checkAndStartActivity(screen, activityIntent, options)
            activity.finish()
        } else {
            fragmentReplace(command)
        }
    }

    protected fun fragmentReplace(command: Replace) {
        val screen = command.screen as SupportAppScreen
        val screenData = generateScreenData(screen.screenKey, localStackCopy)
        val fragment = createFragment(screen)
        if (localStackCopy.size > 0) {
            fragmentManager.popBackStack()
            localStackCopy.dropLast(1)
            val fragmentTransaction =
                fragmentManager.beginTransaction()
            setupFragmentTransaction(
                command,
                fragmentManager.findFragmentById(containerId),
                fragment,
                fragmentTransaction
            )
            fragmentTransaction
                .replace(containerId, fragment, screenData.screenKeyAndCount)
                .addToBackStack(screenData.screenKeyAndCount)
                .commit()
            localStackCopy.add(screenData)
        } else {
            val fragmentTransaction =
                fragmentManager.beginTransaction()
            setupFragmentTransaction(
                command,
                fragmentManager.findFragmentById(containerId),
                fragment,
                fragmentTransaction
            )
            fragmentTransaction
                .replace(containerId, fragment, screenData.screenKeyAndCount)
                .commit()
        }
    }

    /**
     * Performs [BackTo] command transition
     */
    protected open fun backTo(command: BackTo) {
        if (command.screen == null) {
            backToRoot()
        } else {
            backToScreen(command.screen!!.screenKey)
        }
    }

    private fun backToScreen(screenName: String) {
        val index = localStackCopy.indexOfLast { it.screenKey == screenName }

        if (index != -1) {
            val screenData = localStackCopy[index]
            val size = localStackCopy.size
            repeat(size - 1 - index) {
                localStackCopy.dropLast(1)
            }
            onFragmentBack(screenData.screenKeyAndCount)
            fragmentManager.popBackStack(screenData.screenKeyAndCount, 0)
        } else {
            backToUnexisting()
        }
    }

    private fun backToRoot() {
        fragmentManager.popBackStack(
            null,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        localStackCopy.clear()
    }

    private fun closeChain(command: CloseChain) {
        val screenName = command.screen?.screenKey ?: return
        val index = localStackCopy.indexOfLast { it.screenKey == screenName }
        val size = localStackCopy.size
        if (index >= 1) {
            val previousScreen = localStackCopy.getOrNull(index - 1) ?: return
            if (index != -1) {
                for (i in 1 until size - index) {
                    localStackCopy.dropLast(1)
                }
                fragmentManager.popBackStack(previousScreen.screenKeyAndCount, 0)
                onFragmentBack(previousScreen.screenKeyAndCount)
            } else {
                backToUnexisting()
            }
        } else {
            backToRoot()
        }
    }

    protected fun openChain(command: OpenChain) {
        val screens = command.screens
        if (screens.isNotEmpty()) {
            screens.forEachIndexed { index, screen ->
                when {
                    index < screens.size - 1 -> {
                        val screenData = generateScreenData(screen.screenKey, localStackCopy)
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        val fragment = createFragment(screen as SupportAppScreen)
                        fragmentTransaction
                            .add(containerId, fragment, screenData.screenKeyAndCount)
                            .addToBackStack(screenData.screenKeyAndCount)
                            .commit()
                        localStackCopy.add(screenData)
                    }
                    else -> {
                        val screenData = generateScreenData(screen.screenKey, localStackCopy)
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        val fragment = createFragment(screen as SupportAppScreen)
                        setupFragmentTransaction(
                            command,
                            fragmentManager.findFragmentById(containerId),
                            fragment,
                            fragmentTransaction
                        )
                        fragmentTransaction
                            .replace(containerId, fragment, screenData.screenKeyAndCount)
                            .addToBackStack(screenData.screenKeyAndCount)
                            .commit()
                        localStackCopy.add(screenData)
                    }
                }
            }
        }
    }

    /**
     * Override this method to setup fragment transaction [FragmentTransaction].
     * For example: setCustomAnimations(...), addSharedElement(...) or setReorderingAllowed(...)
     *
     * @param command current navigation command. Will be only [Forward] or [Replace]
     * @param currentFragment current fragment in container
     * (for [Replace] command it will be screen previous in new chain, NOT replaced screen)
     * @param nextFragment next screen fragment
     * @param fragmentTransaction fragment transaction
     */
    protected open fun setupFragmentTransaction(
        command: Command?,
        currentFragment: Fragment?,
        nextFragment: Fragment?,
        fragmentTransaction: FragmentTransaction?
    ) {
    }

    /**
     * Override this method to create option for start activity
     *
     * @param command current navigation command. Will be only [Forward] or [Replace]
     * @param activityIntent activity intent
     * @return transition options
     */
    protected fun createStartActivityOptions(
        command: Command?,
        activityIntent: Intent?
    ): Bundle? {
        return null
    }

    private fun checkAndStartActivity(
        screen: SupportAppScreen,
        activityIntent: Intent,
        options: Bundle?
    ) { // Check if we can start activity
        if (activityIntent.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(activityIntent, options)
        } else {
            unexistingActivity(screen, activityIntent)
        }
    }

    /**
     * Called when there is no activity to open `screenKey`.
     *
     * @param screen screen
     * @param activityIntent intent passed to start Activity for the `screenKey`
     */
    protected fun unexistingActivity(
        screen: SupportAppScreen?,
        activityIntent: Intent?
    ) { // Do nothing by default
    }

    /**
     * Creates Fragment matching `screenKey`.
     *
     * @param screen screen
     * @return instantiated fragment for the passed screen
     */
    protected fun createFragment(screen: SupportAppScreen): Fragment {
        val fragment = screen.fragment
        if (fragment == null) {
            errorWhileCreatingScreen(screen)
        }
        return fragment!!
    }

    /**
     * Called when we tried to fragmentBack to some specific screen (via [BackTo] command),
     * but didn't found it.
     */
    protected fun backToUnexisting() {
        backToRoot()
    }

    protected fun errorWhileCreatingScreen(screen: SupportAppScreen) {
        throw RuntimeException("Can't create a screen: " + screen.screenKey)
    }

    private fun generateScreenData(screenKey: String, screens: List<ScreenData>): ScreenData {
        val screenCount = screens.count { it.screenKey == screenKey }
        return ScreenData(
            screenKey = screenKey,
            screenKeyAndCount = "$screenKey$DIVIDER$screenCount"
        )
    }

    private fun fromScreenKeyAndCount(screenKeyAndCount: String): ScreenData {
        return ScreenData(
            screenKey = screenKeyAndCount.substringBefore(DIVIDER),
            screenKeyAndCount = screenKeyAndCount
        )
    }

    private class ScreenData(
        val screenKey: String,
        val screenKeyAndCount: String
    )

    companion object {
        private const val DIVIDER = "#"
    }
}
