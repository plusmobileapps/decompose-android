package com.plusmobileapps.sample.androiddecompose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.plusmobileapps.sample.androiddecompose.data.episodes.Episode
import com.plusmobileapps.sample.androiddecompose.bloc.episodes.EpisodesBloc
import com.plusmobileapps.sample.androiddecompose.ui.theme.DecomposeAndroidSampleTheme
import com.plusmobileapps.sample.androiddecompose.ui.theme.Typography

@Composable
fun EpisodesUI(bloc: EpisodesBloc, paddingValues: PaddingValues) {
    val model = bloc.models.subscribeAsState()
    val episodes = model.value.episodes
    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        items(episodes) {
            EpisodeListItem(episode = it) { bloc.onEpisodeClicked(it) }
        }
    }
}

@Composable
fun EpisodeListItem(episode: Episode, onClick: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp)) {
        Text(text = episode.name, style = Typography.labelLarge)
        Text(text = episode.episode, style = Typography.labelSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.onSurface))
    }
}

@Preview
@Composable
fun EpisodesPreview() {
    DecomposeAndroidSampleTheme {
        Surface {
            EpisodesUI(bloc = object : EpisodesBloc {
                override val models: Value<EpisodesBloc.Model> = MutableValue(
                    EpisodesBloc.Model(
                        isLoading = false,
                        episodes = listOf(
                            Episode(1, name = "The first episode", episode = "S01e01"),
                            Episode(2, name = "The second episode", episode = "S01e02")
                        )
                    )
                )

                override fun onEpisodeClicked(episode: Episode) {
                    TODO("Not yet implemented")
                }
            }, PaddingValues(16.dp))
        }
    }
}