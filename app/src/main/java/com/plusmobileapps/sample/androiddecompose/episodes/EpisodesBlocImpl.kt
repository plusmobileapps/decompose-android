package com.plusmobileapps.sample.androiddecompose.episodes

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.plusmobileapps.sample.androiddecompose.data.episodes.Episode
import com.plusmobileapps.sample.androiddecompose.data.episodes.EpisodeRepository
import com.plusmobileapps.sample.androiddecompose.di.DI
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import com.plusmobileapps.sample.androiddecompose.utils.asValue

class EpisodesBlocImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    dispatchers: Dispatchers,
    repository: EpisodeRepository,
    private val output: (EpisodesBloc.Output) -> Unit
) : EpisodesBloc, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        di: DI,
        output: (EpisodesBloc.Output) -> Unit
    ) : this(
        componentContext = componentContext,
        storeFactory = di.storeFactory,
        dispatchers = di.dispatchers,
        repository = di.episodeRepository,
        output = output
    )

    private val store: EpisodesStore = instanceKeeper.getStore {
        EpisodesStoreProvider(storeFactory, dispatchers, repository).create()
    }

    override val models: Value<EpisodesBloc.Model> = store.asValue().map {
        EpisodesBloc.Model(
            isLoading = it.isLoading,
            episodes = it.episodes,
            error = it.error
        )
    }

    override fun onEpisodeClicked(episode: Episode) {
        output(EpisodesBloc.Output.OpenEpisode(episode))
    }
}