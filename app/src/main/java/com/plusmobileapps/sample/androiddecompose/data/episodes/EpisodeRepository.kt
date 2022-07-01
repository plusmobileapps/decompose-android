package com.plusmobileapps.sample.androiddecompose.data.episodes

import android.util.Log
import com.google.gson.Gson
import com.plusmobileapps.sample.androiddecompose.data.PreferenceDataStore
import com.plusmobileapps.sample.androiddecompose.data.characters.CharactersRepositoryImpl
import com.plusmobileapps.sample.androiddecompose.db.EpisodesQueries
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface EpisodeRepository {
    val hasMoreEpisodesToLoad: Boolean
    fun loadNextPage()
    fun getEpisodes(): Flow<List<Episode>>
    suspend fun getEpisode(id: Int): Episode
}

class EpisodeRepositoryImpl(
    private val service: EpisodeService,
    private val dispatchers: Dispatchers,
    private val db: EpisodesQueries,
    private val preferenceDataStore: PreferenceDataStore
) : EpisodeRepository {

    companion object {
        const val EPISODES_PAGE_KEY = "EPISODES_PAGE_KEY"
        const val TOTAL_PAGES_KEY = "EPISODES_TOTAL_PAGES_KEY"
    }

    private var nextPage = 1
    private var totalPages = Int.MAX_VALUE

    private val job = Job()
    private val scope = CoroutineScope(dispatchers.io + job)

    init {
        scope.launch {
            preferenceDataStore.getIntPreferenceFlow(TOTAL_PAGES_KEY, Int.MAX_VALUE)
                .combine(
                    preferenceDataStore.getIntPreferenceFlow(EPISODES_PAGE_KEY, 1)
                ) { totalPages, lastPageFetched ->
                    this@EpisodeRepositoryImpl.totalPages = totalPages
                    this@EpisodeRepositoryImpl.nextPage = lastPageFetched
                    lastPageFetched
                }.take(1)
                .collect { lastFetchedPage ->
                    if (lastFetchedPage == 1) {
                        scope.launch { fetchPage(1) }
                    }
                    observeLastFetchedPage()
                }
        }
    }

    private fun observeLastFetchedPage() {
        scope.launch {
            preferenceDataStore.getIntPreferenceFlow(EPISODES_PAGE_KEY, 1)
                .collect { lastFetchedPage ->
                    nextPage = lastFetchedPage
                }
            preferenceDataStore.getIntPreferenceFlow(TOTAL_PAGES_KEY, Int.MAX_VALUE)
                .collect { totalPages ->
                    this@EpisodeRepositoryImpl.totalPages = totalPages
                }
        }
    }

    override val hasMoreEpisodesToLoad: Boolean
        get() = nextPage < totalPages

    override fun loadNextPage() {
        scope.launch { fetchPage(nextPage + 1) }
    }

    override fun getEpisodes(): Flow<List<Episode>> =
        db.selectAll()
            .asFlow()
            .mapToList(dispatchers.default)
            .map {
                it.map { episode ->
                    Episode.fromEntity(episode)
                }
            }

    override suspend fun getEpisode(id: Int): Episode = withContext(dispatchers.io) {
        db.getEpisode(id.toLong()).executeAsOneOrNull()?.let {
            Episode.fromEntity(it)
        } ?: throw NoSuchElementException("No episode found for id: $id")
    }

    private suspend fun fetchPage(page: Int) = withContext(dispatchers.io) {
//        if (!hasMoreEpisodesToLoad) return@withContext
        try {
            val response = service.getEpisodes(page.toString())
            db.transaction {
                response.results.forEach { episode ->
                    db.insertEpisode(
                        id = episode.id.toLong(),
                        name = episode.name,
                        air_date = episode.air_date,
                        episode = episode.episode,
                        characters = Gson().toJson(episode.characters),
                        url = episode.url,
                        created = episode.created
                    )
                }
            }
            preferenceDataStore.setIntPreference(EPISODES_PAGE_KEY, page)
            preferenceDataStore.setIntPreference(TOTAL_PAGES_KEY, response.info.pages)
        } catch (e: Exception) {
            Log.e("EpisodesRepository", "fetchPage: ", e)
        }
    }
}