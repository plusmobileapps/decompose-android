package com.plusmobileapps.sample.androiddecompose.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.plusmobileapps.rickandmortysdk.characters.RickAndMortyCharacter
import com.plusmobileapps.sample.androiddecompose.bloc.characters.CharactersBloc
import com.plusmobileapps.sample.androiddecompose.bloc.characters.CharactersListItem
import com.plusmobileapps.sample.androiddecompose.ui.theme.Typography
import com.plusmobileapps.sample.androiddecompose.utils.rememberScrollContext
import kotlinx.coroutines.launch

@Composable
fun CharactersUI(bloc: CharactersBloc, paddingValues: PaddingValues) {
    val model = bloc.models.subscribeAsState()
    val lazyListState = rememberLazyListState()

    val scope = rememberCoroutineScope()
    val scrollContext = rememberScrollContext(lazyListState)

    if (scrollContext.isBottom) {
        bloc.loadMoreCharacters()
    }

    Scaffold(
        modifier = Modifier.padding(paddingValues),
        floatingActionButton = {
            AnimatedVisibility(visible = !scrollContext.isBottom) {
                FloatingActionButton(onClick = {
                    scope.launch {
                        lazyListState.animateScrollToItem(model.value.characters.lastIndex)
                    }
                }) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                }
            }

//            ExtendedFloatingActionButton(text = { Text(text = "Scroll to bottom") }, onClick = {
//                scope.launch {
//                    lazyListState.scrollToItem(model.value.characters.lastIndex)
//                }
//            })
        }
    ) {
        CharactersList(
            paddingValues = it,
            lazyListState = lazyListState,
            characters = model.value.characters,
            onCharacterClicked = bloc::onCharacterClicked
        )
    }


}

@Composable
fun CharactersList(
    paddingValues: PaddingValues,
    lazyListState: LazyListState,
    characters: List<CharactersListItem>,
    onCharacterClicked: (RickAndMortyCharacter) -> Unit,
) {
//    InfiniteLoadingList(
//        modifier = Modifier.padding(paddingValues),
//        listState = lazyListState,
//        items = characters,
//        loadMore = { loadMore() })
//
//    }
    LazyColumn(modifier = Modifier.padding(paddingValues), state = lazyListState) {
        items(characters, key = {
            when (it) {
                is CharactersListItem.Character -> it.value.id
                is CharactersListItem.PageLoading -> CharactersListItem.PageLoading.KEY
            }
        }) {
            when (it) {
                is CharactersListItem.Character -> CharacterListItem(character = it.value) {
                    onCharacterClicked(it.value)
                }
                is CharactersListItem.PageLoading -> Text(
                    "Loading another page",
                    style = Typography.titleMedium
                )
            }

        }
    }
}

@Composable
fun CharacterListItem(character: RickAndMortyCharacter, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            modifier = Modifier.size(120.dp),
            model = character.imageUrl,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(modifier = Modifier.weight(1f), text = character.name, style = Typography.titleMedium)
        Icon(
            Icons.Default.ArrowForward,
            modifier = Modifier.padding(16.dp),
            contentDescription = null
        )
    }
}

//@Preview
//@Composable
//fun CharactersUIPreview() {
//    DecomposeAndroidSampleTheme {
//        Surface {
//            CharactersUI(bloc = object : CharactersBloc {
//                override val models: Value<CharactersBloc.Model> =
//                    MutableValue(CharactersBloc.Model(characters = listOf(
//                        RickAndMortyCharacter(0, "Rick Sanchez", "https://rickandmortyapi.com/api/character/avatar/377.jpeg"),
//                        RickAndMortyCharacter(1, "Morty", "Some Image")
//                    )))
//
//                override fun onCharacterClicked(character: RickAndMortyCharacter) {
//                    TODO("Not yet implemented")
//                }
//
//                override fun loadMoreCharacters() {
//                    TODO("Not yet implemented")
//                }
//            }, paddingValues = PaddingValues(0.dp))
//        }
//    }
//}