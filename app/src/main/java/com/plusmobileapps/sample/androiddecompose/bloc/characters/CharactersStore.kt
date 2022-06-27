package com.plusmobileapps.sample.androiddecompose.bloc.characters

import com.arkivanov.mvikotlin.core.store.Store
import com.plusmobileapps.sample.androiddecompose.bloc.characters.CharactersStore.*

interface CharactersStore : Store<Intent, State, Nothing> {

    data class State(
        val query: String = "",
        val items: List<CharactersListItem> = emptyList(),
        val error: String? = null,
        val isLoading: Boolean = false
    ) {
        val isPageLoading: Boolean =
            items.lastOrNull()?.let { it is CharactersListItem.PageLoading } ?: false
    }

    sealed class Intent {
        object LoadMoreCharacters : Intent()
    }
}