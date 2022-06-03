package com.plusmobileapps.sample.androiddecompose.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.plusmobileapps.sample.androiddecompose.bottomnav.BottomNavBloc
import com.plusmobileapps.sample.androiddecompose.bottomnav.BottomNavBlocImpl
import com.plusmobileapps.sample.androiddecompose.di.DI

class RootBlocImpl(
    componentContext: ComponentContext,
    private val bottomNav: (ComponentContext, (BottomNavBloc.Output) -> Unit) -> BottomNavBloc,
) : RootBloc, ComponentContext by componentContext {

    constructor(componentContext: ComponentContext, di: DI) : this(
        componentContext = componentContext,
        bottomNav = { context, output ->
            BottomNavBlocImpl(
                componentContext = context,
                di = di,
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
        }
    }

    private fun onCharactersOutput(output: BottomNavBloc.Output) {
        when (output) {
            is BottomNavBloc.Output.ShowCharacter -> TODO()
        }
    }

    private sealed class Configuration : Parcelable {
        @Parcelize
        object BottomNav : Configuration()
    }
}