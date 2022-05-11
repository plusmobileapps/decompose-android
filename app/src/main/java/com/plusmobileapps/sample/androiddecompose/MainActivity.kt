package com.plusmobileapps.sample.androiddecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.plusmobileapps.sample.androiddecompose.characters.CharactersBloc
import com.plusmobileapps.sample.androiddecompose.root.RootBloc
import com.plusmobileapps.sample.androiddecompose.ui.theme.DecomposeAndroidSampleTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootBloc: RootBloc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DecomposeAndroidSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootUI(bloc = rootBloc)
                }
            }
        }
    }
}

@Composable
fun RootUI(bloc: RootBloc) {
    Children(routerState = bloc.routerState) {
        when (val child = it.instance) {
            is RootBloc.Child.Characters -> CharactersUI(bloc = child.bloc)
        }
    }
}

@Composable
fun CharactersUI(bloc: CharactersBloc) {
    val model = bloc.models.subscribeAsState()

    Column {
        Text(text = "Query")
        BasicTextField(value = model.value.query, onValueChange = bloc::onQueryChanged)
    }
}