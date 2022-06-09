package com.plusmobileapps.sample.androiddecompose.bloc.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.plusmobileapps.sample.androiddecompose.bloc.bottomnav.BottomNavBloc
import com.plusmobileapps.sample.androiddecompose.bloc.bottomnav.BottomNavBlocImpl
import com.plusmobileapps.sample.androiddecompose.bloc.characterdetail.CharacterDetailBloc
import com.plusmobileapps.sample.androiddecompose.bloc.characterdetail.CharacterDetailBlocImpl
import com.plusmobileapps.sample.androiddecompose.di.DI
import com.plusmobileapps.sample.androiddecompose.bloc.episodedetail.EpisodeDetailBloc
import com.plusmobileapps.sample.androiddecompose.bloc.episodedetail.EpisodeDetailBlocImpl
import com.plusmobileapps.sample.androiddecompose.utils.Consumer

class RootBlocImpl(
    componentContext: ComponentContext,
    private val bottomNav: (ComponentContext, Consumer<BottomNavBloc.Output>) -> BottomNavBloc,
    private val character: (ComponentContext, Int, Consumer<CharacterDetailBloc.Output>) -> CharacterDetailBloc,
    private val episode: (ComponentContext, Int, Consumer<EpisodeDetailBloc.Output>) -> EpisodeDetailBloc,
) : RootBloc, ComponentContext by componentContext {

    constructor(componentContext: ComponentContext, di: DI) : this(
        componentContext = componentContext,
        bottomNav = { context, output ->
            BottomNavBlocImpl(
                componentContext = context,
                di = di,
                output = output
            )
        },
        character = { context, id, output ->
            CharacterDetailBlocImpl(
                context = context,
                di = di,
                id = id,
                output = output
            )
        },
        episode = { context, id, output ->
            EpisodeDetailBlocImpl(
                context = context,
                di = di,
                id = id,
                output = output
            )
        }
    )

    private val router = router<Configuration, RootBloc.Child>(
        initialConfiguration = Configuration.BottomNav,
        handleBackButton = true,
        childFactory = ::createChild,
        key = "RootRouter"
    )

    override val routerState: Value<RouterState<*, RootBloc.Child>> = router.state

    private fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ): RootBloc.Child {
        return when (configuration) {
            Configuration.BottomNav -> RootBloc.Child.BottomNav(
                bottomNav(context, this::onBottomNavOutput)
            )
            is Configuration.Character -> RootBloc.Child.Character(
                character(context, configuration.id, this::onCharacterOutput)
            )
            is Configuration.Episode -> RootBloc.Child.Episode(
                episode(context, configuration.id, this::onEpisodeDetailOutput)
            )
        }
    }

    private fun onCharacterOutput(output: CharacterDetailBloc.Output) {
        when (output) {
            CharacterDetailBloc.Output.Finished -> router.pop()
        }
    }

    private fun onBottomNavOutput(output: BottomNavBloc.Output) {
        when (output) {
            is BottomNavBloc.Output.ShowCharacter -> router.push(Configuration.Character(output.id))
            is BottomNavBloc.Output.ShowEpisode -> router.push(Configuration.Episode(output.id))
        }
    }

    private fun onEpisodeDetailOutput(output: EpisodeDetailBloc.Output) {
        when (output) {
            EpisodeDetailBloc.Output.Finished -> router.pop()
        }
    }

    private sealed class Configuration : Parcelable {
        @Parcelize
        object BottomNav : Configuration()

        @Parcelize
        data class Character(val id: Int) : Configuration()

        @Parcelize
        data class Episode(val id: Int) : Configuration()
    }
}