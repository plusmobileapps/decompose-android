package com.plusmobileapps.sample.androiddecompose.bottomnav

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.bringToFront
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
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

    constructor(componentContext: ComponentContext, di: DI, output: (BottomNavBloc.Output) -> Unit) : this(
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

    override fun onNavItemClicked(item: BottomNavBloc.NavItem) {
        store.accept(BottomNavigationStore.Intent.SelectNavItem(item))
        router.bringToFront(when (item.type) {
            BottomNavBloc.NavItem.Type.CHARACTERS -> Configuration.Characters
            BottomNavBloc.NavItem.Type.EPISODES -> Configuration.Episodes
        })
    }

    private fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ): BottomNavBloc.Child = when (configuration) {
        Configuration.Characters -> BottomNavBloc.Child.Characters(charactersBloc(context, this::onCharactersBlocOutput))
        Configuration.Episodes -> BottomNavBloc.Child.Episodes(object : EpisodesBloc {}) // TODO
    }

    private fun onCharactersBlocOutput(output: CharactersBloc.Output) {
        when(output) {
            is CharactersBloc.Output.OpenCharacter -> this.output(BottomNavBloc.Output.ShowCharacter(output.character.id))
        }
    }

    sealed class Configuration : Parcelable {
        @Parcelize
        object Characters : Configuration()

        @Parcelize
        object Episodes : Configuration()
    }
}