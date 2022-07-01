package com.plusmobileapps.sample.androiddecompose.bloc.episodes

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.plusmobileapps.sample.androiddecompose.bloc.episodes.EpisodesStore.Intent
import com.plusmobileapps.sample.androiddecompose.data.episodes.Episode
import com.plusmobileapps.sample.androiddecompose.data.episodes.EpisodeRepository
import com.plusmobileapps.sample.androiddecompose.bloc.episodes.EpisodesStore.State
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import kotlinx.coroutines.launch

class EpisodesStoreProvider(
    private val storeFactory: StoreFactory,
    private val dispatchers: Dispatchers,
    private val repository: EpisodeRepository
) {

    private sealed class Message {
        object LoadingMore : Message()
        data class EpisodesUpdated(val episodes: List<Episode>) : Message()
        data class Error(val error: String) : Message()
    }

    fun create(): EpisodesStore = object : EpisodesStore,
        Store<Intent, State, Nothing> by storeFactory.create(
            name = "EpisodesStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private inner class ExecutorImpl :
        CoroutineExecutor<Intent, Unit, State, Message, Nothing>(dispatchers.main) {
        override fun executeAction(action: Unit, getState: () -> State) {
            scope.launch {
                repository.getEpisodes().collect { episodes ->
                    dispatch(Message.EpisodesUpdated(episodes))
                }
            }
        }

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.LoadMore -> {
                    if (getState().isLoading) return
                    dispatch(Message.LoadingMore)
                    repository.loadNextPage()
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Message> {
        override fun State.reduce(msg: Message): State =
            when (msg) {
                is Message.EpisodesUpdated -> State(isLoading = false, episodes = msg.episodes)
                is Message.Error -> State(isLoading = false, error = msg.error)
                Message.LoadingMore -> copy(isLoading = true)
            }
    }
}