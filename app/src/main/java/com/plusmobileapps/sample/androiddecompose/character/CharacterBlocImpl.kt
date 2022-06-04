package com.plusmobileapps.sample.androiddecompose.character

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.plusmobileapps.sample.androiddecompose.data.characters.CharactersRepository
import com.plusmobileapps.sample.androiddecompose.di.DI
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import com.plusmobileapps.sample.androiddecompose.utils.asValue

class CharacterBlocImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    dispatchers: Dispatchers,
    repository: CharactersRepository,
    private val id: Int,
    private val output: (CharacterBloc.Output) -> Unit
) : CharacterBloc, ComponentContext by componentContext {

    constructor(
        context: ComponentContext,
        di: DI,
        id: Int,
        output: (CharacterBloc.Output) -> Unit
    ) : this(
        componentContext = context,
        storeFactory = di.storeFactory,
        dispatchers = di.dispatchers,
        repository = di.charactersRepository,
        id = id,
        output = output
    )

    private val store: CharacterStore = instanceKeeper.getStore {
        CharacterStoreProvider(storeFactory, dispatchers, repository, id).create()
    }

    override val models: Value<CharacterBloc.Model> = store.asValue().map {
        CharacterBloc.Model(
            isLoading = it.isLoading,
            name = it.name,
            status = it.status,
            species = it.species,
            image = it.image
        )
    }

    override fun onBackClicked() {
        output(CharacterBloc.Output.Finished)
    }
}