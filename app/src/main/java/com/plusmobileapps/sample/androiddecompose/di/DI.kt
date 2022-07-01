package com.plusmobileapps.sample.androiddecompose.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.plusmobileapps.sample.androiddecompose.BuildConfig
import com.plusmobileapps.sample.androiddecompose.MyApplication
import com.plusmobileapps.sample.androiddecompose.data.PreferenceDataStore
import com.plusmobileapps.sample.androiddecompose.data.PreferenceDataStoreImpl
import com.plusmobileapps.sample.androiddecompose.data.characters.CharacterService
import com.plusmobileapps.sample.androiddecompose.data.characters.CharactersRepository
import com.plusmobileapps.sample.androiddecompose.data.characters.CharactersRepositoryImpl
import com.plusmobileapps.sample.androiddecompose.data.db.createDatabase
import com.plusmobileapps.sample.androiddecompose.data.episodes.EpisodeRepository
import com.plusmobileapps.sample.androiddecompose.data.episodes.EpisodeRepositoryImpl
import com.plusmobileapps.sample.androiddecompose.data.episodes.EpisodeService
import com.plusmobileapps.sample.androiddecompose.db.MyDatabase
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import com.plusmobileapps.sample.androiddecompose.utils.DispatchersImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface DI {
    val database: MyDatabase
    val dispatchers: Dispatchers
    val storeFactory: StoreFactory
    val retrofit: Retrofit
    val characterService: CharacterService
    val charactersRepository: CharactersRepository
    val episodeService: EpisodeService
    val episodeRepository: EpisodeRepository
    val preferenceDataStore: PreferenceDataStore
}

object ServiceLocator : DI {

    override val database: MyDatabase by lazy { createDatabase(MyApplication.context) }

    override val preferenceDataStore: PreferenceDataStore by lazy {
        PreferenceDataStoreImpl(MyApplication.context)
    }
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

    override val charactersRepository: CharactersRepository by lazy {
        CharactersRepositoryImpl(
            service = characterService,
            dispatchers = dispatchers,
            db = database.charactersQueries,
            preferenceDataStore = preferenceDataStore
        )
    }

    override val episodeService: EpisodeService by lazy { retrofit.create(EpisodeService::class.java) }

    override val episodeRepository: EpisodeRepository by lazy {
        EpisodeRepositoryImpl(
            service = episodeService,
            dispatchers = dispatchers,
            db = database.episodesQueries,
            preferenceDataStore = preferenceDataStore
        )
    }
}