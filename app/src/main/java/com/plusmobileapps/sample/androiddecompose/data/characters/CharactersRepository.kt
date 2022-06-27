package com.plusmobileapps.sample.androiddecompose.data.characters

import android.util.Log
import com.plusmobileapps.sample.androiddecompose.db.Character
import com.plusmobileapps.sample.androiddecompose.db.CharactersQueries
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface CharactersRepository {
    val hasMoreCharactersToLoad: Boolean
    fun loadNextPage()
    suspend fun getCharacters(): Flow<List<RickAndMortyCharacter>>
    suspend fun getCharacter(id: Int): RickAndMortyCharacter
}

class CharactersRepositoryImpl(
    private val service: CharacterService,
    private val dispatchers: Dispatchers,
    private val db: CharactersQueries
) : CharactersRepository {

    private var nextPage = 1

    private val job = Job()
    private val scope = CoroutineScope(dispatchers.io + job)

    init {
        loadNextPage()
    }

    override var hasMoreCharactersToLoad: Boolean = true
        private set

    override fun loadNextPage() {
        scope.launch {
            fetchCharacters(page = nextPage)
        }
    }

    private suspend fun fetchCharacters(page: Int) {
        try {
            val response = service.getCharacters(page.toString())
            val characters = response.results.map { RickAndMortyCharacter.fromDTO(it) }
            db.transaction {
                characters.forEach { character ->
                    db.insertCharacter(
                        id = character.id.toLong(),
                        name = character.name,
                        imageUrl = character.imageUrl,
                        status = character.status,
                        species = character.species
                    )
                }
            }
            nextPage += 1
        } catch (e: Exception) {
            Log.e("CharactersRepository", "Could fetch characters", e)
        }

    }

    override suspend fun getCharacters(): Flow<List<RickAndMortyCharacter>> =
        db.selectAll()
            .asFlow()
            .mapToList()
            .map { characters ->
                characters.map { character ->
                    RickAndMortyCharacter(
                        id = character.id.toInt(),
                        name = character.name,
                        imageUrl = character.imageUrl,
                        status = character.status,
                        species = character.species
                    )
                }
            }

    override suspend fun getCharacter(id: Int): RickAndMortyCharacter =
        withContext(dispatchers.io) {
            val character = db.getCharacter(id.toLong()).executeAsOne()
            RickAndMortyCharacter(
                id = character.id.toInt(),
                name = character.name,
                imageUrl = character.imageUrl,
                status = character.status,
                species = character.species
            )
        }
}