package com.plusmobileapps.sample.androiddecompose.characters

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.plusmobileapps.sample.androiddecompose.characters.CharactersStore.Intent
import com.plusmobileapps.sample.androiddecompose.characters.CharactersStore.State
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers

class CharactersStoreProvider(
    private val storeFactory: StoreFactory,
    private val dispatchers: Dispatchers
) {

    private sealed class Message {
        data class CharactersUpdated(val characters: List<Character>) : Message()
        data class QueryUpdated(val query: String) : Message()
        object Loading : Message()
    }

    fun provide(): CharactersStore = object : CharactersStore,
        Store<Intent, State, Nothing> by storeFactory.create<Intent, Unit, Message, State, Nothing>(
            name = "CharactersStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private inner class ExecutorImpl :
        CoroutineExecutor<Intent, Unit, State, Message, Nothing>(dispatchers.main) {

        override fun executeAction(action: Unit, getState: () -> State) {
        }

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.ExecuteQuery -> TODO()
                is Intent.UpdateQuery -> dispatch(Message.QueryUpdated(intent.query))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Message> {
        override fun State.reduce(msg: Message): State =
            when (msg) {
                is Message.Loading -> copy(isLoading = true)
                is Message.CharactersUpdated -> copy(
                    characters = msg.characters,
                    isLoading = false
                )
                is Message.QueryUpdated -> copy(query = msg.query)
            }
    }
}