package com.plusmobileapps.sample.androiddecompose.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.plusmobileapps.sample.androiddecompose.characters.CharactersBloc
import com.plusmobileapps.sample.androiddecompose.characters.CharactersBlocImpl
import com.plusmobileapps.sample.androiddecompose.utils.consumer

class RootBlocImpl(
    componentContext: ComponentContext,
    private val characters: (ComponentContext, Consumer<CharactersBloc.Output>) -> CharactersBloc,
) : RootBloc, ComponentContext by componentContext {

    constructor(componentContext: ComponentContext, storeFactory: StoreFactory): this(
        componentContext = componentContext,
        characters = { context, output ->
            CharactersBlocImpl(
                componentContext = context,
                storeFactory = storeFactory,
                output = output
            )
        }
    )

    private val router = router<Configuration, RootBloc.Child>(
        initialConfiguration = Configuration.Characters,
        handleBackButton = true,
        childFactory = ::createChild,
        key = "RootRouter"
    )
    override val routerState: Value<RouterState<*, RootBloc.Child>> = router.state

    private fun createChild(configuration: Configuration, context: ComponentContext): RootBloc.Child {
        return when (configuration) {
            Configuration.Characters -> RootBloc.Child.Characters(characters(context, consumer(this::onCharactersOutput)))
        }
    }

    private fun onCharactersOutput(output: CharactersBloc.Output) {
        when (output) {
            is CharactersBloc.Output.OpenCharacter -> TODO("router.push() character detail page")
        }
    }

    private sealed class Configuration : Parcelable {
        @Parcelize
        object Characters : Configuration()
    }
}