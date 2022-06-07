package com.plusmobileapps.sample.androiddecompose.bloc.episodedetail

import com.arkivanov.decompose.value.Value
import com.plusmobileapps.sample.androiddecompose.data.episodes.Episode

interface EpisodeDetailBloc {

    val models: Value<Model>

    fun onBackArrowClicked()

    data class Model(val episode: Episode, val isLoading: Boolean)

    sealed class Output {
        object Finished : Output()
    }

}