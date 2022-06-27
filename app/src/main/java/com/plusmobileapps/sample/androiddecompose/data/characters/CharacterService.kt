package com.plusmobileapps.sample.androiddecompose.data.characters

import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterService {

    @GET("api/character/")
    suspend fun getCharacters(@Query("page") page: String): CharactersResponse

}