package ru.terrakok.cicerone

import android.view.View
import androidx.transition.Transition
import androidx.transition.TransitionSet
import com.example.carexplorer.helpers.navigation.*
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.BackTo

fun Router.navigateToScreen(screen: Screen, withAnimation: Boolean = true) {
    if (withAnimation) {
        commandBuffer.executeCommands(arrayOf(AnimatedForward(screen)))
    } else navigateTo(screen)
}

fun Router.closeChainExecutor(chainStartScreen: Screen) {
    commandBuffer.executeCommands(arrayOf(CloseChain(chainStartScreen)))
}

fun Router.backToScreen(screen: Screen, withAnimation: Boolean = true) {
    if (withAnimation) {
        commandBuffer.executeCommands(arrayOf(AnimatedBackTo(screen)))
    } else backTo(screen)
}

fun Router.backToFirstScreen(screen: Screen) {
    commandBuffer.executeCommands(arrayOf(AnimatedBackToFirst()))
}

fun Router.replaceScreen(screen: Screen, withAnimation: Boolean = true) {
    if (withAnimation) {
        commandBuffer.executeCommands(arrayOf(AnimatedReplace(screen)))
    } else replaceScreen(screen)
}

fun Router.newRootScreen(screen: Screen) {
    commandBuffer.executeCommands(arrayOf(BackTo(null), AnimatedReplace(screen)))
}

fun Router.exitDefault() {
    commandBuffer.executeCommands((arrayOf(AnimatedBack())))
}

fun Router.sharedElementTransition(
    screen: SupportAppScreen,
    views: List<View>,
    sharedTransition: TransitionSet,
    returnSharedTransition: TransitionSet,
    enterNewFragmentTransition: Transition? = null,
    returnNewFragmentTransition: Transition? = null,
    reenterCurrentFragmentTransition: Transition? = null,
    exitCurrentFragmentTransition: Transition? = null
) {
    commandBuffer.executeCommands(
        arrayOf(
            SharedElementCommand(
                screen,
                views,
                sharedTransition,
                returnSharedTransition,
                enterNewFragmentTransition,
                returnNewFragmentTransition,
                reenterCurrentFragmentTransition,
                exitCurrentFragmentTransition
            )
        )
    )
}

