package com.example.carexplorer.helpers.navigation

import android.view.View
import androidx.transition.Transition
import androidx.transition.TransitionSet
import com.example.carexplorer.R
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.*

interface AnimatedCommand {
    val oldScreenAnimation: Int?
    val newScreenAnimation: Int?
}

class SharedElementCommand(
    val screen: SupportAppScreen,
    val views: List<View>,
    val sharedTransition: TransitionSet,
    val returnSharedTransition: TransitionSet,
    val enterNewFragmentTransition: Transition? = null,
    val returnNewFragmentTransition: Transition? = null,
    val reenterCurrentFragmentTransition: Transition? = null,
    val exitCurrentFragmentTransition: Transition? = null
) : Forward(screen)

open class BackToFirst : Command
open class CloseChain(val screen: Screen?) : Command
open class OpenChain(val screens: List<Screen>) : Command

class AnimatedBackToFirst(
    override val oldScreenAnimation: Int? = R.anim.exit_to_right,
    override val newScreenAnimation: Int? = R.anim.enter_from_left
) : BackToFirst(), AnimatedCommand

class AnimatedBack(
    override val oldScreenAnimation: Int? = R.anim.exit_to_right,
    override val newScreenAnimation: Int? = R.anim.enter_from_left
) : Back(), AnimatedCommand

class AnimatedBackTo(
    newScreen: Screen?,
    override val oldScreenAnimation: Int? = R.anim.exit_to_right,
    override val newScreenAnimation: Int? = R.anim.enter_from_left
) : BackTo(newScreen), AnimatedCommand

class AnimatedCloseChain(
    newScreen: Screen?,
    override val oldScreenAnimation: Int? = R.anim.exit_to_right,
    override val newScreenAnimation: Int? = R.anim.enter_from_left
) : CloseChain(newScreen), AnimatedCommand

class AnimatedOpenChain(
    newScreen: List<Screen>,
    override val oldScreenAnimation: Int? = R.anim.exit_to_left,
    override val newScreenAnimation: Int? = R.anim.enter_from_right
) : OpenChain(newScreen), AnimatedCommand

class AnimatedForward(
    newScreen: Screen,
    override val oldScreenAnimation: Int? = R.anim.exit_to_left,
    override val newScreenAnimation: Int? = R.anim.enter_from_right
) : Forward(newScreen), AnimatedCommand

class AnimatedReplace(
    newScreen: Screen,
    override val oldScreenAnimation: Int? = R.anim.exit_to_left,
    override val newScreenAnimation: Int? = R.anim.enter_from_right
) : Replace(newScreen), AnimatedCommand