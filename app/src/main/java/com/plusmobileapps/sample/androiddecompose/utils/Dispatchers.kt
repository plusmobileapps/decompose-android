package com.plusmobileapps.sample.androiddecompose.utils

import kotlinx.coroutines.CoroutineDispatcher

interface Dispatchers {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}

internal class DispatchersImpl : Dispatchers {
    override val main: CoroutineDispatcher = kotlinx.coroutines.Dispatchers.Main
    override val io: CoroutineDispatcher = kotlinx.coroutines.Dispatchers.IO
    override val default: CoroutineDispatcher = kotlinx.coroutines.Dispatchers.Default
}