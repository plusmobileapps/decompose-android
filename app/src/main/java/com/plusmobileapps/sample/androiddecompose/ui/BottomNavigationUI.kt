package com.plusmobileapps.sample.androiddecompose.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.arkivanov.decompose.extensions.compose.jetpack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.animation.child.childAnimation
import com.arkivanov.decompose.extensions.compose.jetpack.animation.child.fade
import com.arkivanov.decompose.extensions.compose.jetpack.animation.child.plus
import com.arkivanov.decompose.extensions.compose.jetpack.animation.child.scale
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.plusmobileapps.sample.androiddecompose.bloc.bottomnav.BottomNavBloc
import com.plusmobileapps.sample.androiddecompose.bloc.bottomnav.BottomNavigationStore
import com.plusmobileapps.sample.androiddecompose.ui.theme.DecomposeAndroidSampleTheme

@Composable
fun BottomNavUI(bloc: BottomNavBloc) {
    Scaffold(bottomBar = {
        BottomNavigationBar(bloc.models, bloc::onNavItemClicked)
    }) { paddingValues ->
        Children(routerState = bloc.routerState, animation = childAnimation(fade() + scale())) {
            when (val child = it.instance) {
                is BottomNavBloc.Child.Characters -> CharactersUI(bloc = child.bloc, paddingValues)
                is BottomNavBloc.Child.Episodes -> EpisodesUI(bloc = child.bloc, paddingValues)
                is BottomNavBloc.Child.About -> AboutUI()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(models: Value<BottomNavBloc.Model>, onClick: (BottomNavBloc.NavItem) -> Unit) {
    val model = models.subscribeAsState()
    val items = model.value.navItems
    NavigationBar {
        items.forEach {
            NavigationBarItem(
                selected = it.selected,
                onClick = { onClick(it) },
                icon = {
                    Icon(
                        painter = rememberAsyncImagePainter(it.type.icon),
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(id = it.type.label)) }
            )
        }
    }
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
    DecomposeAndroidSampleTheme {
        Surface {
            BottomNavigationBar(MutableValue(BottomNavBloc.Model(BottomNavigationStore.State().navItems))) {}
        }
    }
}