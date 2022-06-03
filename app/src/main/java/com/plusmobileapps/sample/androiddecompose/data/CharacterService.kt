package com.plusmobileapps.sample.androiddecompose.data

import retrofit2.http.GET

interface CharacterService {

    @GET("api/character/")
    suspend fun getCharacters(): CharactersResponse

}