package com.plusmobileapps.sample.androiddecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetpack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.plusmobileapps.sample.androiddecompose.characters.CharactersBloc
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
            is RootBloc.Child.Characters -> CharactersUI(bloc = child.bloc)
        }
    }
}

@Composable
fun CharactersUI(bloc: CharactersBloc) {
    val model = bloc.models.subscribeAsState()

    Text(text = "Characters screen")
}