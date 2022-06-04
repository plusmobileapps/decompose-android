package com.plusmobileapps.sample.androiddecompose.data.episodes

import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import kotlinx.coroutines.withContext

interface EpisodeRepository {
    suspend fun getEpisodes(): List<Episode>
    suspend fun getEpisode(id: Int): Episode
}

class EpisodeRepositoryImpl(
    private val service: EpisodeService,
    private val dispatchers: Dispatchers
) : EpisodeRepository {

    private var episodes = mapOf<Int, Episode>()

    override suspend fun getEpisodes(): List<Episode> = withContext(dispatchers.io) {
        service.getEpisodes().results.also {
            episodes = it.associateBy { episode -> episode.id }
        }
    }

    override suspend fun getEpisode(id: Int): Episode = withContext(dispatchers.io) {
        episodes[id] ?: throw NoSuchElementException("No episode found for id: $id")
    }
}