package com.plusmobileapps.sample.androiddecompose.episodedetail

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.plusmobileapps.sample.androiddecompose.data.episodes.Episode
import com.plusmobileapps.sample.androiddecompose.data.episodes.EpisodeRepository
import com.plusmobileapps.sample.androiddecompose.episodedetail.EpisodeDetailStore.State
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import kotlinx.coroutines.launch

class EpisodeDetailStoreProvider(
    private val storeFactory: StoreFactory,
    private val dispatchers: Dispatchers,
    private val repository: EpisodeRepository,
    private val id: Int
) {

    sealed class Message {
        data class EpisodeUpdated(val episode: Episode) : Message()
    }

    fun create(): EpisodeDetailStore =
        object : EpisodeDetailStore, Store<Nothing, State, Nothing> by storeFactory.create(
            name = "CharacterStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private inner class ExecutorImpl :
        CoroutineExecutor<Nothing, Unit, State, Message, Nothing>(dispatchers.main) {
        override fun executeAction(action: Unit, getState: () -> State) {
            scope.launch {
                val episode = repository.getEpisode(id)
                dispatch(Message.EpisodeUpdated(episode))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Message> {
        override fun State.reduce(msg: Message): State =
            when (msg) {
                is Message.EpisodeUpdated -> State(episode = msg.episode, isLoading = false)
            }
    }

}