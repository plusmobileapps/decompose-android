package com.plusmobileapps.sample.androiddecompose.data.episodes

import retrofit2.http.GET
import retrofit2.http.Query

interface EpisodeService {

    @GET("api/episode")
    suspend fun getEpisodes(@Query("page") page: String): EpisodesResponse
}