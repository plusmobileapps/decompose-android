package com.plusmobileapps.sample.androiddecompose.di

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleOwner
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.plusmobileapps.sample.androiddecompose.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object StoreFactoryModule {

    @Provides
    @Singleton
    fun provideStoreFactory(): StoreFactory = if (BuildConfig.DEBUG) {
        LoggingStoreFactory(DefaultStoreFactory())
    } else {
        DefaultStoreFactory()
    }

}

@Module
@InstallIn(ActivityComponent::class)
object ComponentContextModule {
    @ApplicationComponentContext
    @Provides
    fun provideApplicationComponentContext(activity: Activity): ComponentContext {
        activity as? ComponentActivity
            ?: throw IllegalStateException("Activity isn't a ComponentActivity")
        return DefaultComponentContext(
            lifecycle = (activity as LifecycleOwner).lifecycle,
            savedStateRegistry = activity.savedStateRegistry,
            viewModelStore = activity.viewModelStore,
            onBackPressedDispatcher = activity.onBackPressedDispatcher
        )
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationComponentContext