package com.plusmobileapps.sample.androiddecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetpack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.animation.child.*
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.plusmobileapps.sample.androiddecompose.bottomnav.BottomNavBloc
import com.plusmobileapps.sample.androiddecompose.character.CharacterBloc
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
    Children(routerState = bloc.routerState, animation = childAnimation(slide())) {
        when (val child = it.instance) {
            is RootBloc.Child.BottomNav -> BottomNavUI(bloc = child.bloc)
            is RootBloc.Child.Character -> CharacterDetailUI(bloc = child.bloc)
        }
    }
}

@Composable
fun BottomNavUI(bloc: BottomNavBloc) {
    Scaffold(bottomBar = {
        BottomNavigationBar(bloc = bloc)
    }) { paddingValues ->
        Children(routerState = bloc.routerState, animation = childAnimation(fade() + scale())) {
            when (val child = it.instance) {
                is BottomNavBloc.Child.Characters -> CharactersUI(bloc = child.bloc, paddingValues)
                is BottomNavBloc.Child.Episodes -> Text("Episodes")
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
fun CharactersUI(bloc: CharactersBloc, paddingValues: PaddingValues) {
    val model = bloc.models.subscribeAsState()
    val characters = model.value.characters

    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        items(characters) {
            CharacterListItem(character = it) { bloc.onCharacterClicked(it) }
        }
    }

}

@Composable
fun CharacterListItem(character: RickAndMortyCharacter, onClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }) {
        AsyncImage(model = character.imageUrl, contentDescription = null)
        Text(text = character.name)
    }
}

@Composable
fun CharacterDetailUI(bloc: CharacterBloc) {
    val model = bloc.models.subscribeAsState()
    val state = model.value
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = stringResource(R.string.character_detail_title)) },
                navigationIcon = {
                    IconButton(onClick = bloc::onBackClicked,) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(model = state.image, contentDescription = null)
            Text(state.name)
        }
    }
}