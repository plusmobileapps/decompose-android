package com.plusmobileapps.sample.androiddecompose

import android.app.Application
import android.content.Context

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
    }

    companion object {
        lateinit var context: Context
    }
}