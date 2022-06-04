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
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.plusmobileapps.sample.androiddecompose.R
import com.plusmobileapps.sample.androiddecompose.episodedetail.EpisodeDetailBloc
import com.plusmobileapps.sample.androiddecompose.ui.theme.Typography

@Composable
fun EpisodeDetailUI(bloc: EpisodeDetailBloc) {
    val model = bloc.models.subscribeAsState()
    val state = model.value
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = stringResource(R.string.episode_detail_title)) },
                navigationIcon = {
                    IconButton(onClick = bloc::onBackArrowClicked) {
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
            Text(state.episode.name, style = Typography.headlineMedium, textAlign = TextAlign.Center)
            Text(state.episode.episode, style = Typography.bodyMedium, textAlign = TextAlign.Center)
            Text(state.episode.url, style = Typography.bodyMedium, textAlign = TextAlign.Center)
            Text(state.episode.created, style = Typography.bodyMedium, textAlign = TextAlign.Center)
        }
    }
}