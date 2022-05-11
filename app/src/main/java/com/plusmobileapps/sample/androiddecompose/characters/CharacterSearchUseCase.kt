package com.plusmobileapps.sample.androiddecompose.characters

import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.singleOf
import com.badoo.reaktive.single.subscribeOn
import javax.inject.Inject

class CharacterSearchUseCase @Inject constructor() {

    fun fetchSuperheroes(query: String): Single<List<Character>> = singleOf(
        listOf(
            Character(1L, "some name", ""),
            Character(2L, "some other name", "")
        )
    ).subscribeOn(ioScheduler)
}