package com.plusmobileapps.sample.androiddecompose.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.plusmobileapps.sample.androiddecompose.BuildConfig
import com.plusmobileapps.sample.androiddecompose.data.CharacterService
import com.plusmobileapps.sample.androiddecompose.data.CharactersRepository
import com.plusmobileapps.sample.androiddecompose.data.CharactersRepositoryImpl
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import com.plusmobileapps.sample.androiddecompose.utils.DispatchersImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface DI {
    val apiKey: String
    val dispatchers: Dispatchers
    val storeFactory: StoreFactory
    val retrofit: Retrofit
    val characterService: CharacterService
    val repository: CharactersRepository
}

object ServiceLocator : DI {

    override val apiKey: String = BuildConfig.API_KEY

    override val dispatchers: Dispatchers = DispatchersImpl()

    override val storeFactory: StoreFactory = if (BuildConfig.DEBUG) {
        LoggingStoreFactory(DefaultStoreFactory())
    } else {
        DefaultStoreFactory()
    }

    override val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override val characterService: CharacterService = retrofit.create(CharacterService::class.java)

    override val repository: CharactersRepository = CharactersRepositoryImpl(
        service = characterService,
        dispatchers = dispatchers
    )

}