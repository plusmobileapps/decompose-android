package com.plusmobileapps.sample.androiddecompose.bottomnav

import com.arkivanov.mvikotlin.core.store.Store
import com.plusmobileapps.sample.androiddecompose.bottomnav.BottomNavBloc.NavItem
import com.plusmobileapps.sample.androiddecompose.bottomnav.BottomNavBloc.NavItem.Type.CHARACTERS
import com.plusmobileapps.sample.androiddecompose.bottomnav.BottomNavBloc.NavItem.Type.EPISODES

interface BottomNavigationStore :
    Store<BottomNavigationStore.Intent, BottomNavigationStore.State, Nothing> {

    sealed class Intent {
        data class SelectNavItem(val navItem: NavItem) : Intent()
    }

    data class State(
        val navItems: List<NavItem> = listOf(
            NavItem(true, CHARACTERS),
            NavItem(false, EPISODES)
        )
    )
}