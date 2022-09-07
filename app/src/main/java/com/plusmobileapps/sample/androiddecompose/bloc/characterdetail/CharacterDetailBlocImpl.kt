package com.plusmobileapps.sample.androiddecompose.bloc.characterdetail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.plusmobileapps.rickandmortysdk.characters.CharactersStore
import com.plusmobileapps.sample.androiddecompose.data.characters.CharactersRepository
import com.plusmobileapps.sample.androiddecompose.di.DI
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import com.plusmobileapps.sample.androiddecompose.utils.asValue

class CharacterDetailBlocImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    dispatchers: Dispatchers,
    repository: CharactersStore,
    private val id: Int,
    private val output: (CharacterDetailBloc.Output) -> Unit
) : CharacterDetailBloc, ComponentContext by componentContext {

    constructor(
        context: ComponentContext,
        di: DI,
        id: Int,
        output: (CharacterDetailBloc.Output) -> Unit
    ) : this(
        componentContext = context,
        storeFactory = di.storeFactory,
        dispatchers = di.dispatchers,
        repository = di.rickAndMorty.charactersStore,
        id = id,
        output = output
    )

    private val store: CharacterDetailStore = instanceKeeper.getStore {
        CharacterDetailStoreProvider(storeFactory, dispatchers, repository, id).create()
    }

    override val models: Value<CharacterDetailBloc.Model> = store.asValue().map {
        CharacterDetailBloc.Model(
            isLoading = it.isLoading,
            name = it.name,
            status = it.status,
            species = it.species,
            image = it.image
        )
    }

    override fun onBackClicked() {
        output(CharacterDetailBloc.Output.Finished)
    }
}