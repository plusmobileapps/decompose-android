package com.plusmobileapps.sample.androiddecompose.bloc.root

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import com.plusmobileapps.sample.androiddecompose.bloc.bottomnav.BottomNavBloc
import com.plusmobileapps.sample.androiddecompose.bloc.characterdetail.CharacterDetailBloc
import com.plusmobileapps.sample.androiddecompose.bloc.episodedetail.EpisodeDetailBloc

interface RootBloc {
    val routerState: Value<RouterState<*, Child>>

    sealed class Child {
        data class BottomNav(val bloc: BottomNavBloc) : Child()
        data class Character(val bloc: CharacterDetailBloc) : Child()
        data class Episode(val bloc: EpisodeDetailBloc) : Child()
    }
}