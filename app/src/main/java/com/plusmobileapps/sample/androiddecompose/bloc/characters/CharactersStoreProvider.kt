package com.plusmobileapps.sample.androiddecompose.bloc.characters

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.plusmobileapps.sample.androiddecompose.bloc.characters.CharactersStore.Intent
import com.plusmobileapps.sample.androiddecompose.bloc.characters.CharactersStore.State
import com.plusmobileapps.sample.androiddecompose.data.characters.CharactersRepository
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import kotlinx.coroutines.launch

class CharactersStoreProvider(
    private val storeFactory: StoreFactory,
    private val dispatchers: Dispatchers,
    private val repository: CharactersRepository
) {

    private sealed class Message {
        data class CharactersUpdated(val items: List<CharactersListItem>) : Message()
        data class LoadingNextPage(val hasMore: Boolean) : Message()
    }

    fun provide(): CharactersStore = object : CharactersStore,
        Store<Intent, State, Nothing> by storeFactory.create(
            name = "CharactersStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private inner class ExecutorImpl :
        CoroutineExecutor<Intent, Unit, State, Message, Nothing>(dispatchers.main) {

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.LoadMoreCharacters -> loadMoreCharacters(getState)
            }
        }

        override fun executeAction(action: Unit, getState: () -> State) {
            observeCharacters()
        }

        private fun observeCharacters() {
            scope.launch {
                repository.getCharacters().collect { characters ->
                    dispatch(Message.CharactersUpdated(characters.map(CharactersListItem::Character)))
                }
            }
        }

        private fun loadMoreCharacters(getState: () -> State) {
            val state = getState()
            if (state.isLoading || state.isPageLoading) {
                return
            }
            dispatch(Message.LoadingNextPage(hasMore = repository.hasMoreCharactersToLoad))
            repository.loadNextPage()
        }
    }

    private object ReducerImpl : Reducer<State, Message> {
        override fun State.reduce(msg: Message): State =
            when (msg) {
                is Message.CharactersUpdated -> copy(
                    items = msg.items,
                    isLoading = false
                )
                is Message.LoadingNextPage -> copy(
                    items = items + CharactersListItem.PageLoading(isLoading = true, hasMore = msg.hasMore)
                )
            }
    }
}