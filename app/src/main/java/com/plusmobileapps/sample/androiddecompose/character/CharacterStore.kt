package com.plusmobileapps.sample.androiddecompose.character

import com.arkivanov.mvikotlin.core.store.Store
import com.plusmobileapps.sample.androiddecompose.character.CharacterStore.State

interface CharacterStore : Store<Nothing, State, Nothing> {

    data class State(
        val isLoading: Boolean = true,
        val id: Int = 0,
        val name: String = "",
        val status: String = "",
        val species: String = "",
        val image: String = ""
    )

}