package com.plusmobileapps.sample.androiddecompose.bloc.characterdetail

import com.arkivanov.mvikotlin.core.store.Store
import com.plusmobileapps.sample.androiddecompose.bloc.characterdetail.CharacterDetailStore.State

interface CharacterDetailStore : Store<Nothing, State, Nothing> {

    data class State(
        val isLoading: Boolean = true,
        val id: Int = 0,
        val name: String = "",
        val status: String = "",
        val species: String = "",
        val image: String = ""
    )

}