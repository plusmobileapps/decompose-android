package com.plusmobileapps.sample.androiddecompose.bloc.episodes

import com.arkivanov.decompose.value.Value
import com.plusmobileapps.sample.androiddecompose.data.episodes.Episode

interface EpisodesBloc {

    val models: Value<Model>

    fun onEpisodeClicked(episode: Episode)

    fun loadMore()

    data class Model(
        val isLoading: Boolean,
        val episodes: List<Episode>,
        val error: String? = null
    )

    sealed class Output {
        data class OpenEpisode(val episode: Episode) : Output()
    }
}