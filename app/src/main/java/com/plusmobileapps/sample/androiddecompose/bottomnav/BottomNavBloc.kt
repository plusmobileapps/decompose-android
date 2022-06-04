package com.plusmobileapps.sample.androiddecompose.bottomnav

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import com.plusmobileapps.sample.androiddecompose.R
import com.plusmobileapps.sample.androiddecompose.characters.CharactersBloc
import com.plusmobileapps.sample.androiddecompose.episodes.EpisodesBloc

interface BottomNavBloc {

    val routerState: Value<RouterState<*, Child>>

    val models: Value<Model>

    fun onNavItemClicked(item: NavItem)

    data class Model(val navItems: List<NavItem>)

    data class NavItem(val selected: Boolean, val type: Type) {
        enum class Type(val id: Long, @StringRes val label: Int, @DrawableRes val icon: Int) {
            CHARACTERS(1L, R.string.bottom_nav_characters, R.drawable.ic_groups_fill),
            EPISODES(2L, R.string.bottom_nav_episodes, R.drawable.ic_subscriptions_fill),
            ABOUT(3L, R.string.bottom_nav_about, R.drawable.ic_info_fill)
        }
    }
    sealed class Child {
        data class Characters(val bloc: CharactersBloc) : Child()
        data class Episodes(val bloc: EpisodesBloc) : Child()
        object About : Child()
    }

    sealed class Output {
        data class ShowCharacter(val id: Int) : Output()
    }
}