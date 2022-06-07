package com.plusmobileapps.sample.androiddecompose.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.plusmobileapps.sample.androiddecompose.bloc.characterdetail.CharacterDetailBloc
import com.plusmobileapps.sample.androiddecompose.R
import com.plusmobileapps.sample.androiddecompose.ui.theme.DecomposeAndroidSampleTheme
import com.plusmobileapps.sample.androiddecompose.ui.theme.Typography

@Composable
fun CharacterDetailUI(bloc: CharacterDetailBloc) {
    val model = bloc.models.subscribeAsState()
    val state = model.value
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = stringResource(R.string.character_detail_title)) },
                navigationIcon = {
                    IconButton(onClick = bloc::onBackClicked) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(model = state.image, contentDescription = null)
            Text(state.name, style = Typography.headlineMedium, textAlign = TextAlign.Center)
            Text(state.species, style = Typography.bodyMedium, textAlign = TextAlign.Center)
            Text(state.status, style = Typography.bodyMedium, textAlign = TextAlign.Center)
        }
    }
}

@Preview
@Composable
fun CharacterDetailUIPreview() {
    DecomposeAndroidSampleTheme {
        Surface {
            CharacterDetailUI(bloc = object : CharacterDetailBloc {
                override val models: Value<CharacterDetailBloc.Model> = MutableValue(
                    CharacterDetailBloc.Model(
                        name = "Morty with some really long name to see what happens",
                        status = "Alive",
                        species = "Human",
                        image = "https://rickandmortyapi.com/api/character/avatar/377.jpeg"
                    )
                )

                override fun onBackClicked() {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}