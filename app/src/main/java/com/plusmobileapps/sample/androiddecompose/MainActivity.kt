package com.plusmobileapps.sample.androiddecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetpack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.animation.child.childAnimation
import com.arkivanov.decompose.extensions.compose.jetpack.animation.child.slide
import com.plusmobileapps.sample.androiddecompose.di.ServiceLocator
import com.plusmobileapps.sample.androiddecompose.bloc.root.RootBloc
import com.plusmobileapps.sample.androiddecompose.bloc.root.RootBlocImpl
import com.plusmobileapps.sample.androiddecompose.ui.BottomNavUI
import com.plusmobileapps.sample.androiddecompose.ui.CharacterDetailUI
import com.plusmobileapps.sample.androiddecompose.ui.EpisodeDetailUI
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
            is RootBloc.Child.Episode -> EpisodeDetailUI(bloc = child.bloc)
        }
    }
}