package com.plusmobileapps.sample.androiddecompose.bloc.characters

import com.plusmobileapps.sample.androiddecompose.data.characters.RickAndMortyCharacter

sealed class CharactersListItem {
    data class Character(val value: RickAndMortyCharacter) : CharactersListItem()
    data class PageLoading(val isLoading: Boolean, val hasMore: Boolean) : CharactersListItem()
}
