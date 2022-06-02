package com.plusmobileapps.sample.androiddecompose.util

import com.plusmobileapps.sample.androiddecompose.utils.Dispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher

class TestDispatchers : Dispatchers {
    val testDispatcher = StandardTestDispatcher()

    override val main: CoroutineDispatcher = testDispatcher
    override val io: CoroutineDispatcher = testDispatcher
    override val default: CoroutineDispatcher = testDispatcher
}