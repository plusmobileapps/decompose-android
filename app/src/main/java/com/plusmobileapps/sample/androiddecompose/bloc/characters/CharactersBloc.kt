package com.plusmobileapps.sample.androiddecompose.bloc.characters

import com.arkivanov.decompose.value.Value
import com.plusmobileapps.rickandmortysdk.characters.RickAndMortyCharacter

interface CharactersBloc {

    val models: Value<Model>

    fun loadMoreCharacters()

    fun onCharacterClicked(character: RickAndMortyCharacter)

    data class Model(
        val characters: List<CharactersListItem> = emptyList(),
        val error: String? = null,
        val isLoading: Boolean = false
    )

    sealed class Output {
        data class OpenCharacter(val character: RickAndMortyCharacter) : Output()
    }

}