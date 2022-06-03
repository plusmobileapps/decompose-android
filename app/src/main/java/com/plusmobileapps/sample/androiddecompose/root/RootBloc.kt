package com.plusmobileapps.sample.androiddecompose.root

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import com.plusmobileapps.sample.androiddecompose.bottomnav.BottomNavBloc
import com.plusmobileapps.sample.androiddecompose.character.CharacterBloc

interface RootBloc {
    val routerState: Value<RouterState<*, Child>>

    sealed class Child {
        data class BottomNav(val bloc: BottomNavBloc) : Child()
        data class Character(val bloc: CharacterBloc) : Child()
    }
}