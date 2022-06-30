package com.plusmobileapps.sample.androiddecompose.data.characters

import android.util.Log
import com.plusmobileapps.sample.androiddecompose.data.PreferenceDataStore
import com.plusmobileapps.sample.androiddecompose.db.Character
import com.plusmobileapps.sample.androiddecompose.db.CharactersQueries
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
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
    private val db: CharactersQueries,
    private val preferenceDataStore: PreferenceDataStore
) : CharactersRepository {

    companion object {
        const val CHARACTERS_PAGE_KEY = "CHARACTERS_PAGE_KEY"
        const val TOTAL_PAGES_KEY = "CHARACTER_TOTAL_PAGES_KEY"
    }

    private var nextPage = 1
    private var totalPages = Int.MAX_VALUE

    private val job = Job()
    private val scope = CoroutineScope(dispatchers.io + job)

    init {
        scope.launch {
            preferenceDataStore.getIntPreferenceFlow(TOTAL_PAGES_KEY, Int.MAX_VALUE)
                .combine(
                    preferenceDataStore.getIntPreferenceFlow(CHARACTERS_PAGE_KEY, 1)
                ) { totalPages, lastPageFetched ->
                    this@CharactersRepositoryImpl.totalPages = totalPages
                    this@CharactersRepositoryImpl.nextPage = lastPageFetched
                    lastPageFetched
                }.take(1)
                .collect { lastFetchedPage ->
                    if (lastFetchedPage == 1) {
                        loadNextPage()
                    }
                    observeLastFetchedPage()
                }
        }
    }

    private fun observeLastFetchedPage() {
        scope.launch {
            preferenceDataStore.getIntPreferenceFlow(CHARACTERS_PAGE_KEY, 1)
                .collect { lastFetchedPage ->
                    nextPage = lastFetchedPage
                }
            preferenceDataStore.getIntPreferenceFlow(TOTAL_PAGES_KEY, Int.MAX_VALUE)
                .collect { totalPages ->
                    this@CharactersRepositoryImpl.totalPages = totalPages
                }
        }
    }

    override val hasMoreCharactersToLoad: Boolean
        get() = nextPage < totalPages

    override fun loadNextPage() {
        scope.launch {
            fetchCharacters(page = nextPage)
        }
    }

    private suspend fun fetchCharacters(page: Int) {
        if (!hasMoreCharactersToLoad) return
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
            preferenceDataStore.setIntPreference(CHARACTERS_PAGE_KEY, page + 1)
            preferenceDataStore.setIntPreference(TOTAL_PAGES_KEY, response.info.pages)
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