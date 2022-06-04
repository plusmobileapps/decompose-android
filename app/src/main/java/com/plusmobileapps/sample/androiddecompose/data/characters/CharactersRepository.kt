package com.plusmobileapps.sample.androiddecompose.data.characters

import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import kotlinx.coroutines.withContext

interface CharactersRepository {
    suspend fun getCharacters(): List<RickAndMortyCharacter>
    suspend fun getCharacter(id: Int): RickAndMortyCharacter
}

class CharactersRepositoryImpl(
    private val service: CharacterService,
    private val dispatchers: Dispatchers
) : CharactersRepository {

    private var characters = mapOf<Int, RickAndMortyCharacter>()

    override suspend fun getCharacters(): List<RickAndMortyCharacter> =
        withContext(dispatchers.io) {
            val response = service.getCharacters()
            response.results.map { RickAndMortyCharacter.fromDTO(it) }
        }.also { characters = it.associateBy { character -> character.id } }

    override suspend fun getCharacter(id: Int): RickAndMortyCharacter = withContext(dispatchers.io) {
        characters[id] ?: throw NoSuchElementException("No character found for id: $id")
    }
}