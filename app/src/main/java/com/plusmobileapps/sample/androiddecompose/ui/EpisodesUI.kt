package com.plusmobileapps.sample.androiddecompose.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.plusmobileapps.sample.androiddecompose.episodes.EpisodesBloc
import com.plusmobileapps.sample.androiddecompose.ui.theme.DecomposeAndroidSampleTheme

@Composable
fun EpisodesUI(bloc: EpisodesBloc, paddingValues: PaddingValues) {
    Text(text = "Episodes")
}

@Preview
@Composable
fun EpisodesPreview() {
    DecomposeAndroidSampleTheme {
        Surface {
            EpisodesUI(bloc = object : EpisodesBloc{}, PaddingValues(16.dp))
        }
    }
}