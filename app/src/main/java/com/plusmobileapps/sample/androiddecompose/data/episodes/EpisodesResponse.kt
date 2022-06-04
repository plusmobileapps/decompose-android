package com.plusmobileapps.sample.androiddecompose.data.episodes

data class EpisodesResponse(
    val info: EpisodesInfo,
    val results: List<Episode>
)

data class EpisodesInfo(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

data class Episode(
    val id: Int = 0,
    val name: String = "",
    val air_date: String = "",
    val episode: String = "",
    val characters: List<String> = emptyList(),
    val url: String = "",
    val created: String = ""
)
