package com.plusmobileapps.sample.androiddecompose.characters

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.plusmobileapps.sample.androiddecompose.utils.asValue
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class CharactersBloc @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted private val output: Consumer<Output>,
    charactersStoreProvider: CharactersStoreProvider
) : ComponentContext by componentContext {

    @AssistedFactory
    interface Factory {
        fun create(context: ComponentContext, output: Consumer<Output>): CharactersBloc
    }

    private val store = instanceKeeper.getStore { charactersStoreProvider.provide() }

    val models: Value<Model> = store.asValue().map { state ->
        Model(
            query = state.query,
            characters = state.characters,
            error = state.error,
            isLoading = state.isLoading
        )
    }

    fun onCharacterClicked(character: Character) {
        output.onNext(Output.OpenCharacter(character))
    }

    fun onQueryChanged(query: String) {
        store.accept(CharactersStore.Intent.UpdateQuery(query))
    }

    fun onClearQueryClicked() {
        store.accept(CharactersStore.Intent.UpdateQuery(""))
    }

    fun onSearchClicked() {
        store.accept(CharactersStore.Intent.ExecuteQuery)
    }

    data class Model(
        val query: String = "",
        val characters: List<Character> = emptyList(),
        val error: String? = null,
        val isLoading: Boolean = false
    )

    sealed class Output {
        data class OpenCharacter(val character: Character) : Output()
    }


}