package com.plusmobileapps.sample.androiddecompose.characters

import com.arkivanov.decompose.value.Value

interface CharactersBloc {

    val models: Value<Model>

    fun onCharacterClicked(character: Character)

    fun onQueryChanged(query: String)

    fun onClearQueryClicked()

    fun onSearchClicked()

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