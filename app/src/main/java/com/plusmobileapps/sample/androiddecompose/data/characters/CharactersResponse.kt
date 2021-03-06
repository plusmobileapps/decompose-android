package com.plusmobileapps.sample.androiddecompose.data.characters

data class CharactersResponse(
    val info: CharactersInfo,
    val results: List<CharacterDTO>
)

data class CharactersInfo(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

data class CharacterDTO(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gener: String,
    val origin: CharacterOrigin,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
)

data class CharacterOrigin(
    val name: String,
    val url: String
)