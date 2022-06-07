package com.plusmobileapps.sample.androiddecompose.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.plusmobileapps.sample.androiddecompose.BuildConfig
import com.plusmobileapps.sample.androiddecompose.data.characters.CharacterService
import com.plusmobileapps.sample.androiddecompose.data.characters.CharactersRepository
import com.plusmobileapps.sample.androiddecompose.data.characters.CharactersRepositoryImpl
import com.plusmobileapps.sample.androiddecompose.data.episodes.EpisodeRepository
import com.plusmobileapps.sample.androiddecompose.data.episodes.EpisodeRepositoryImpl
import com.plusmobileapps.sample.androiddecompose.data.episodes.EpisodeService
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import com.plusmobileapps.sample.androiddecompose.utils.DispatchersImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface DI {
    val dispatchers: Dispatchers
    val storeFactory: StoreFactory
    val retrofit: Retrofit
    val characterService: CharacterService
    val charactersRepository: CharactersRepository
    val episodeService: EpisodeService
    val episodeRepository: EpisodeRepository
}

object ServiceLocator : DI {

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

    override val charactersRepository: CharactersRepository = CharactersRepositoryImpl(
        service = characterService,
        dispatchers = dispatchers
    )

    override val episodeService: EpisodeService by lazy { retrofit.create(EpisodeService::class.java) }

    override val episodeRepository: EpisodeRepository by lazy {
        EpisodeRepositoryImpl(
            service = episodeService,
            dispatchers = dispatchers
        )
    }
}