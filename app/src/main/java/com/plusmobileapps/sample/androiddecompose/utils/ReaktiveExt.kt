package com.plusmobileapps.sample.androiddecompose.utils

import com.badoo.reaktive.base.Consumer

/**
 * Factory function for creating a [Consumer]
 */
inline fun <T> consumer(crossinline block: (T) -> Unit): Consumer<T> =
    object : Consumer<T> {
        override fun onNext(value: T) {
            block(value)
        }
    }