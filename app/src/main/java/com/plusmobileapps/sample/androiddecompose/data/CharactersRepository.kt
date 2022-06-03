package com.plusmobileapps.sample.androiddecompose.data

import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import kotlinx.coroutines.withContext

interface CharactersRepository {
    suspend fun getCharacters(): List<RickAndMortyCharacter>
}

class CharactersRepositoryImpl(
    private val service: CharacterService,
    private val dispatchers: Dispatchers
) : CharactersRepository {

    override suspend fun getCharacters(): List<RickAndMortyCharacter> =
        withContext(dispatchers.io) {
            val response = service.getCharacters()
            response.results.map { RickAndMortyCharacter.fromDTO(it) }
        }
}