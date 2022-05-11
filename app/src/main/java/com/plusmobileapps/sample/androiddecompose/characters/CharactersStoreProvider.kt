package com.plusmobileapps.sample.androiddecompose.characters

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.reaktive.reaktiveBootstrapper
import com.arkivanov.mvikotlin.extensions.reaktive.reaktiveExecutorFactory
import com.plusmobileapps.sample.androiddecompose.characters.CharactersStore.Intent
import com.plusmobileapps.sample.androiddecompose.characters.CharactersStore.State

class CharactersStoreProvider(private val storeFactory: StoreFactory) {

    sealed class Message {
        data class CharactersUpdated(val characters: List<Character>) : Message()
        data class QueryUpdated(val query: String): Message()
        object Loading : Message()
    }

    fun provide(): CharactersStore = object : CharactersStore, Store<Intent, State, Nothing> by storeFactory.create<Intent, Unit, Message, State, Nothing>(
        name = "CharactersStore",
        initialState = State(),
        bootstrapper = reaktiveBootstrapper {

        },
        executorFactory = reaktiveExecutorFactory {
            onIntent<Intent> { intent ->
                when (intent) {
                    Intent.ExecuteQuery -> TODO()
                    is Intent.UpdateQuery -> dispatch(Message.QueryUpdated(intent.query))
                }
            }
        },
        reducer = { msg ->
            when (msg) {
                is Message.Loading -> copy(isLoading = true)
                is Message.CharactersUpdated -> copy(characters = msg.characters, isLoading = false)
                is Message.QueryUpdated -> copy(query = msg.query)
            }
        }
    ) {}
}