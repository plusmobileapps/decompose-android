package com.plusmobileapps.sample.androiddecompose.characters

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.plusmobileapps.sample.androiddecompose.di.DI
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import com.plusmobileapps.sample.androiddecompose.utils.asValue

class CharactersBlocImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    dispatchers: Dispatchers,
    private val output: (CharactersBloc.Output) -> Unit
) : CharactersBloc, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        di: DI,
        output: (CharactersBloc.Output) -> Unit
    ) : this(
        componentContext = componentContext,
        storeFactory = di.storeFactory,
        dispatchers = di.dispatchers,
        output = output
    )

    private val store = instanceKeeper.getStore {
        CharactersStoreProvider(storeFactory, dispatchers).provide()
    }

    override val models: Value<CharactersBloc.Model> = store.asValue().map { state ->
        CharactersBloc.Model(
            query = state.query,
            characters = state.characters,
            error = state.error,
            isLoading = state.isLoading
        )
    }

    override fun onCharacterClicked(character: Character) {
        output(CharactersBloc.Output.OpenCharacter(character))
    }

    override fun onQueryChanged(query: String) {
        store.accept(CharactersStore.Intent.UpdateQuery(query))
    }

    override fun onClearQueryClicked() {
        store.accept(CharactersStore.Intent.UpdateQuery(""))
    }

    override fun onSearchClicked() {
        store.accept(CharactersStore.Intent.ExecuteQuery)
    }
}