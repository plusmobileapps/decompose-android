package com.plusmobileapps.sample.androiddecompose.bloc.characters

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.plusmobileapps.rickandmortysdk.RickAndMortySdk
import com.plusmobileapps.rickandmortysdk.characters.RickAndMortyCharacter
import com.plusmobileapps.sample.androiddecompose.di.DI
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import com.plusmobileapps.sample.androiddecompose.utils.asValue

class CharactersBlocImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    dispatchers: Dispatchers,
    rickAndMorty: RickAndMortySdk,
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
        rickAndMorty = di.rickAndMorty,
        output = output
    )

    private val store: CharactersStore = instanceKeeper.getStore {
        CharactersStoreProvider(storeFactory, dispatchers, rickAndMorty).provide()
    }

    override val models: Value<CharactersBloc.Model> = store.asValue().map { state ->
        CharactersBloc.Model(
            characters = state.items,
            error = state.error,
            isLoading = state.isLoading
        )
    }

    override fun loadMoreCharacters() {
        store.accept(CharactersStore.Intent.LoadMoreCharacters)
    }

    override fun onCharacterClicked(character: RickAndMortyCharacter) {
        output(CharactersBloc.Output.OpenCharacter(character))
    }
}