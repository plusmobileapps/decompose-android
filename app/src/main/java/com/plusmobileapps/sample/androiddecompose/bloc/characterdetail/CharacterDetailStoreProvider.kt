package com.plusmobileapps.sample.androiddecompose.bloc.characterdetail

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.plusmobileapps.rickandmortysdk.characters.CharactersStore
import com.plusmobileapps.rickandmortysdk.characters.RickAndMortyCharacter
import com.plusmobileapps.sample.androiddecompose.bloc.characterdetail.CharacterDetailStore.State
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import kotlinx.coroutines.launch

class CharacterDetailStoreProvider(
    private val storeFactory: StoreFactory,
    private val dispatchers: Dispatchers,
    private val repository: CharactersStore,
    private val id: Int
) {

    private sealed class Message {
        data class CharacterUpdated(val character: RickAndMortyCharacter) : Message()
    }

    fun create(): CharacterDetailStore = object : CharacterDetailStore,
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
                    image = msg.character.imageUrl,
                    status = msg.character.status,
                    species = msg.character.species
                )
            }
    }

}