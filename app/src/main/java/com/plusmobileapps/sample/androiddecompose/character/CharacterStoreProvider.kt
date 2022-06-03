package com.plusmobileapps.sample.androiddecompose.character

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.plusmobileapps.sample.androiddecompose.character.CharacterStore.State
import com.plusmobileapps.sample.androiddecompose.data.CharactersRepository
import com.plusmobileapps.sample.androiddecompose.data.RickAndMortyCharacter
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import kotlinx.coroutines.launch

class CharacterStoreProvider(
    private val storeFactory: StoreFactory,
    private val dispatchers: Dispatchers,
    private val repository: CharactersRepository,
    private val id: Int
) {

    private sealed class Message {
        data class CharacterUpdated(val character: RickAndMortyCharacter) : Message()
    }

    fun create(): CharacterStore = object : CharacterStore,
        Store<Nothing, State, Nothing> by storeFactory.create(
            name = "CharacterStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<Nothing, Unit, State, Message, Nothing>(dispatchers.main) {
        override fun executeAction(action: Unit, getState: () -> State) {
            scope.launch {
                val character = repository.getCharacter(id)
                dispatch(Message.CharacterUpdated(character))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Message> {
        override fun State.reduce(msg: Message): State =
            when (msg) {
                is Message.CharacterUpdated -> State(
                    isLoading = false,
                    id = msg.character.id,
                    name = msg.character.name,
                    image = msg.character.imageUrl
                )
            }
    }

}