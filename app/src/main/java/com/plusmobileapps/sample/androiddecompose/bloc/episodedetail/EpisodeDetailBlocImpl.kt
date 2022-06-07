package com.plusmobileapps.sample.androiddecompose.bloc.episodedetail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.plusmobileapps.sample.androiddecompose.data.episodes.EpisodeRepository
import com.plusmobileapps.sample.androiddecompose.di.DI
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import com.plusmobileapps.sample.androiddecompose.utils.asValue

class EpisodeDetailBlocImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    dispatchers: Dispatchers,
    repository: EpisodeRepository,
    id: Int,
    private val output: (EpisodeDetailBloc.Output) -> Unit
) : EpisodeDetailBloc, ComponentContext by componentContext {

    constructor(context: ComponentContext, di: DI, id: Int, output: (EpisodeDetailBloc.Output) -> Unit): this(
        componentContext = context,
        storeFactory = di.storeFactory,
        dispatchers = di.dispatchers,
        repository = di.episodeRepository,
        id = id,
        output = output
    )

    private val store: EpisodeDetailStore = instanceKeeper.getStore {
        EpisodeDetailStoreProvider(storeFactory, dispatchers, repository, id).create()
    }

    override val models: Value<EpisodeDetailBloc.Model> = store.asValue().map {
        EpisodeDetailBloc.Model(
            episode = it.episode,
            isLoading = it.isLoading
        )
    }

    override fun onBackArrowClicked() {
        output(EpisodeDetailBloc.Output.Finished)
    }
}