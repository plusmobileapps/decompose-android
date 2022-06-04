package com.plusmobileapps.sample.androiddecompose.characters

import com.arkivanov.decompose.value.Value
import com.plusmobileapps.sample.androiddecompose.data.characters.RickAndMortyCharacter

interface CharactersBloc {

    val models: Value<Model>

    fun onCharacterClicked(character: RickAndMortyCharacter)

    fun onQueryChanged(query: String)

    fun onClearQueryClicked()

    fun onSearchClicked()

    data class Model(
        val query: String = "",
        val characters: List<RickAndMortyCharacter> = emptyList(),
        val error: String? = null,
        val isLoading: Boolean = false
    )

    sealed class Output {
        data class OpenCharacter(val character: RickAndMortyCharacter) : Output()
    }

}