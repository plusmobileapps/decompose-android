package com.plusmobileapps.sample.androiddecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetpack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.plusmobileapps.sample.androiddecompose.bottomnav.BottomNavBloc
import com.plusmobileapps.sample.androiddecompose.characters.CharactersBloc
import com.plusmobileapps.sample.androiddecompose.data.RickAndMortyCharacter
import com.plusmobileapps.sample.androiddecompose.di.ServiceLocator
import com.plusmobileapps.sample.androiddecompose.root.RootBloc
import com.plusmobileapps.sample.androiddecompose.root.RootBlocImpl
import com.plusmobileapps.sample.androiddecompose.ui.theme.DecomposeAndroidSampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = buildRoot(defaultComponentContext())
        setContent {
            DecomposeAndroidSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootUI(bloc = root)
                }
            }
        }
    }

    private fun buildRoot(componentContext: ComponentContext): RootBloc = RootBlocImpl(
        componentContext = componentContext,
        di = ServiceLocator
    )
}

@Composable
fun RootUI(bloc: RootBloc) {
    Children(routerState = bloc.routerState) {
        when (val child = it.instance) {
            is RootBloc.Child.BottomNav -> BottomNavUI(bloc = child.bloc)
        }
    }
}

@Composable
fun BottomNavUI(bloc: BottomNavBloc) {
    Scaffold(bottomBar = {
        BottomNavigationBar(bloc = bloc)
    }) {
        Children(routerState = bloc.routerState) {
            when (val child = it.instance) {
                is BottomNavBloc.Child.Characters -> CharactersUI(bloc = child.bloc)
                is BottomNavBloc.Child.Episodes -> TODO()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(bloc: BottomNavBloc) {
    val model = bloc.models.subscribeAsState()
    val items = model.value.navItems
    NavigationBar {
        items.forEach {
            NavigationBarItem(
                selected = it.selected,
                onClick = { bloc.onNavItemClicked(it) },
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


@Composable
fun CharactersUI(bloc: CharactersBloc) {
    val model = bloc.models.subscribeAsState()
    val characters = model.value.characters

    LazyColumn {
        items(characters) {
            CharacterListItem(character = it)
        }
    }

}

@Composable
fun CharacterListItem(character: RickAndMortyCharacter) {
    Row(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(model = character.imageUrl, contentDescription = null)
        Text(text = character.name)
    }
}