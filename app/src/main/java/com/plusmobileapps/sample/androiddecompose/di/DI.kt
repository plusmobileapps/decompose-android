package com.plusmobileapps.sample.androiddecompose.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.plusmobileapps.sample.androiddecompose.BuildConfig
import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import com.plusmobileapps.sample.androiddecompose.utils.DispatchersImpl

interface DI {
    val dispatchers: Dispatchers
    val storeFactory: StoreFactory
}

object ServiceLocator : DI {
    override val dispatchers: Dispatchers = DispatchersImpl()
    override val storeFactory: StoreFactory = if (BuildConfig.DEBUG) {
        LoggingStoreFactory(DefaultStoreFactory())
    } else {
        DefaultStoreFactory()
    }
}