package com.plusmobileapps.sample.androiddecompose.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.plusmobileapps.sample.androiddecompose.bottomnav.BottomNavBloc
import com.plusmobileapps.sample.androiddecompose.bottomnav.BottomNavBlocImpl
import com.plusmobileapps.sample.androiddecompose.character.CharacterBloc
import com.plusmobileapps.sample.androiddecompose.character.CharacterBlocImpl
import com.plusmobileapps.sample.androiddecompose.di.DI

class RootBlocImpl(
    componentContext: ComponentContext,
    private val bottomNav: (ComponentContext, (BottomNavBloc.Output) -> Unit) -> BottomNavBloc,
    private val character: (ComponentContext, Int, (CharacterBloc.Output) -> Unit) -> CharacterBloc
) : RootBloc, ComponentContext by componentContext {

    constructor(componentContext: ComponentContext, di: DI) : this(
        componentContext = componentContext,
        bottomNav = { context, output ->
            BottomNavBlocImpl(
                componentContext = context,
                di = di,
                output = output
            )
        },
        character = { context, id, output ->
            CharacterBlocImpl(
                context = context,
                di = di,
                id = id,
                output = output
            )
        }
    )

    private val router = router<Configuration, RootBloc.Child>(
        initialConfiguration = Configuration.BottomNav,
        handleBackButton = true,
        childFactory = ::createChild,
        key = "RootRouter"
    )

    override val routerState: Value<RouterState<*, RootBloc.Child>> = router.state

    private fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ): RootBloc.Child {
        return when (configuration) {
            Configuration.BottomNav -> RootBloc.Child.BottomNav(
                bottomNav(context, this::onCharactersOutput)
            )
            is Configuration.Character -> RootBloc.Child.Character(
                character(context, configuration.id, this::onCharacterOutput)
            )
        }
    }

    private fun onCharacterOutput(output: CharacterBloc.Output) {
        when (output) {
            CharacterBloc.Output.Finished -> router.pop()
        }
    }

    private fun onCharactersOutput(output: BottomNavBloc.Output) {
        when (output) {
            is BottomNavBloc.Output.ShowCharacter -> router.push(Configuration.Character(output.id))
        }
    }

    private sealed class Configuration : Parcelable {
        @Parcelize
        object BottomNav : Configuration()

        @Parcelize
        data class Character(val id: Int) : Configuration()
    }
}