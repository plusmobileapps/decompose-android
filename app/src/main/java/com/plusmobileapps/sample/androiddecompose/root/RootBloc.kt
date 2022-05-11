package com.plusmobileapps.sample.androiddecompose.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.plusmobileapps.sample.androiddecompose.characters.CharactersBloc
import com.plusmobileapps.sample.androiddecompose.di.ApplicationComponentContext
import com.plusmobileapps.sample.androiddecompose.utils.consumer
import javax.inject.Inject

class RootBloc @Inject constructor(
    @ApplicationComponentContext componentContext: ComponentContext,
    private val charactersBlocFactory: CharactersBloc.Factory,
) : ComponentContext by componentContext {

    private val router = router<Configuration, Child>(
        initialConfiguration = Configuration.Characters,
        handleBackButton = true,
        childFactory = ::createChild,
        key = "RootRouter"
    )
    val routerState: Value<RouterState<*, Child>> = router.state

    private fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ): Child {
        return when (configuration) {
            Configuration.Characters -> Child.Characters(
                charactersBlocFactory.create(
                    context = context,
                    output = consumer(this::onCharactersOutput)
                )
            )
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

    sealed class Child {
        data class Characters(val bloc: CharactersBloc) : Child()
    }
}