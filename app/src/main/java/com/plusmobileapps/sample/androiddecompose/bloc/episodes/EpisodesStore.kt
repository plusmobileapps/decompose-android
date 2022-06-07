package com.plusmobileapps.sample.androiddecompose.bloc.episodes

import com.arkivanov.mvikotlin.core.store.Store
import com.plusmobileapps.sample.androiddecompose.data.episodes.Episode
import com.plusmobileapps.sample.androiddecompose.bloc.episodes.EpisodesStore.State

interface EpisodesStore : Store<Nothing, State, Nothing> {
    data class State(
        val isLoading: Boolean = true,
        val episodes: List<Episode> = emptyList(),
        val error: String? = null
    )
}