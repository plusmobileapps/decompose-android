package com.plusmobileapps.sample.androiddecompose.characters

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.plusmobileapps.sample.androiddecompose.characters.CharactersStore.Intent
import com.plusmobileapps.sample.androiddecompose.characters.CharactersStore.State
import com.plusmobileapps.sample.androiddecompose.data.characters.CharactersRepository
import com.plusmobileapps.sample.androiddecompose.data.characters.RickAndMortyCharacter
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import kotlinx.coroutines.launch

class CharactersStoreProvider(
    private val storeFactory: StoreFactory,
    private val dispatchers: Dispatchers,
    private val repository: CharactersRepository
) {

    private sealed class Message {
        data class CharactersUpdated(val characters: List<RickAndMortyCharacter>) : Message()
        data class QueryUpdated(val query: String) : Message()
        object Loading : Message()
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

        override fun executeAction(action: Unit, getState: () -> State) {
            scope.launch {
                try {
                    val characters = repository.getCharacters()
                    dispatch(Message.CharactersUpdated(characters))
                } catch (e: Exception) {
                    Log.e("CharactersStore", "COuldn't fetch characters", e)
                }
            }
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