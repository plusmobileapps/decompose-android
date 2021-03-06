package com.plusmobileapps.sample.androiddecompose.bloc.episodedetail

import com.arkivanov.mvikotlin.core.store.Store
import com.plusmobileapps.sample.androiddecompose.data.episodes.Episode
import com.plusmobileapps.sample.androiddecompose.bloc.episodedetail.EpisodeDetailStore.State

interface EpisodeDetailStore : Store<Nothing, State, Nothing> {

    data class State(val episode: Episode = Episode(), val isLoading: Boolean = true)

}