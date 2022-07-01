package com.plusmobileapps.sample.androiddecompose.data.episodes

import com.google.gson.Gson
import com.plusmobileapps.sample.androiddecompose.db.Episodes

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
) {
    companion object {
        fun fromEntity(episode: Episodes): Episode  = Episode(
            id = episode.id.toInt(),
            name = episode.name,
            air_date = episode.air_date,
            episode = episode.episode,
            characters = Gson().fromJson(episode.characters, Array<String>::class.java).toList(),
            url = episode.url,
            created = episode.created
        )
    }
}
