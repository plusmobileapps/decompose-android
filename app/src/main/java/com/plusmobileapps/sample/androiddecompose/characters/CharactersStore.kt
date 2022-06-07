package com.plusmobileapps.sample.androiddecompose.characters

import com.arkivanov.mvikotlin.core.store.Store
import com.plusmobileapps.sample.androiddecompose.characters.CharactersStore.*
import com.plusmobileapps.sample.androiddecompose.data.characters.RickAndMortyCharacter

interface CharactersStore : Store<Nothing, State, Nothing> {

    data class State(
        val query: String = "",
        val characters: List<RickAndMortyCharacter> = emptyList(),
        val error: String? = null,
        val isLoading: Boolean = false
    )
}