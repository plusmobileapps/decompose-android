package com.plusmobileapps.sample.androiddecompose.characters

import com.arkivanov.mvikotlin.core.store.Store
import com.plusmobileapps.sample.androiddecompose.characters.CharactersStore.*

interface CharactersStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        data class UpdateQuery(val query: String) : Intent()
        object ExecuteQuery : Intent()
    }

    data class State(
        val query: String = "",
        val characters: List<Character> = emptyList(),
        val error: String? = null,
        val isLoading: Boolean = false
    )
}