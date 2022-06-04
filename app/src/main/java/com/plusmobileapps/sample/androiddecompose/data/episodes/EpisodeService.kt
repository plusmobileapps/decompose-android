package com.plusmobileapps.sample.androiddecompose.data.episodes

import retrofit2.http.GET

interface EpisodeService {

    @GET("api/episode")
    suspend fun getEpisodes(): EpisodesResponse
}