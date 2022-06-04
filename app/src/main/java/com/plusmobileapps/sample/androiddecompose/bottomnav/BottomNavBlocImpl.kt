package com.plusmobileapps.sample.androiddecompose.bottomnav

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.bringToFront
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.doOnPause
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.plusmobileapps.sample.androiddecompose.characters.CharactersBloc
import com.plusmobileapps.sample.androiddecompose.characters.CharactersBlocImpl
import com.plusmobileapps.sample.androiddecompose.di.DI
import com.plusmobileapps.sample.androiddecompose.episodes.EpisodesBloc
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import com.plusmobileapps.sample.androiddecompose.utils.asValue

class BottomNavBlocImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    dispatchers: Dispatchers,
    private val charactersBloc: (ComponentContext, (CharactersBloc.Output) -> Unit) -> CharactersBloc,
    private val output: (BottomNavBloc.Output) -> Unit
) : BottomNavBloc, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        di: DI,
        output: (BottomNavBloc.Output) -> Unit
    ) : this(
        componentContext = componentContext,
        storeFactory = di.storeFactory,
        dispatchers = di.dispatchers,
        charactersBloc = { context, characterOutput ->
            CharactersBlocImpl(
                componentContext = context,
                di = di,
                output = characterOutput
            )
        },
        output = output
    )

    private val store: BottomNavigationStore = instanceKeeper.getStore {
        BottomNavStoreProvider(storeFactory, dispatchers).create()
    }

    private val router = router<Configuration, BottomNavBloc.Child>(
        initialConfiguration = Configuration.Characters,
        handleBackButton = true,
        childFactory = ::createChild,
        key = "BottomNavRouter"
    )

    override val routerState: Value<RouterState<*, BottomNavBloc.Child>> = router.state

    override val models: Value<BottomNavBloc.Model> = store.asValue().map {
        BottomNavBloc.Model(it.navItems)
    }

    private val routerSubscriber: (RouterState<Configuration, BottomNavBloc.Child>) -> Unit = {
        store.accept(
            BottomNavigationStore.Intent.SelectNavItem(
                when (it.activeChild.instance) {
                    is BottomNavBloc.Child.Characters -> BottomNavBloc.NavItem.Type.CHARACTERS
                    is BottomNavBloc.Child.Episodes -> BottomNavBloc.NavItem.Type.EPISODES
                    is BottomNavBloc.Child.About -> BottomNavBloc.NavItem.Type.ABOUT
                }
            )
        )
    }

    init {
        lifecycle.doOnResume {
            router.state.subscribe(routerSubscriber)
        }
        lifecycle.doOnPause {
            router.state.unsubscribe(routerSubscriber)
        }
    }

    override fun onNavItemClicked(item: BottomNavBloc.NavItem) {
        router.bringToFront(
            when (item.type) {
                BottomNavBloc.NavItem.Type.CHARACTERS -> Configuration.Characters
                BottomNavBloc.NavItem.Type.EPISODES -> Configuration.Episodes
                BottomNavBloc.NavItem.Type.ABOUT -> Configuration.About
            }
        )
    }

    private fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ): BottomNavBloc.Child = when (configuration) {
        Configuration.Characters -> BottomNavBloc.Child.Characters(
            charactersBloc(
                context,
                this::onCharactersBlocOutput
            )
        )
        Configuration.Episodes -> BottomNavBloc.Child.Episodes(object : EpisodesBloc {}) // TODO
        Configuration.About -> BottomNavBloc.Child.About
    }

    private fun onCharactersBlocOutput(output: CharactersBloc.Output) {
        when (output) {
            is CharactersBloc.Output.OpenCharacter -> this.output(
                BottomNavBloc.Output.ShowCharacter(
                    output.character.id
                )
            )
        }
    }

    sealed class Configuration : Parcelable {
        @Parcelize
        object Characters : Configuration()

        @Parcelize
        object Episodes : Configuration()

        @Parcelize
        object About : Configuration()
    }
}