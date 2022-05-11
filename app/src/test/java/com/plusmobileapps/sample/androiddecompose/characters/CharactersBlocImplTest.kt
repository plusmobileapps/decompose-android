package com.plusmobileapps.sample.androiddecompose.characters

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.scheduler.overrideSchedulers
import com.badoo.reaktive.subject.publish.PublishSubject
import com.badoo.reaktive.test.observable.assertValue
import com.badoo.reaktive.test.observable.test
import com.badoo.reaktive.test.scheduler.TestScheduler
import com.plusmobileapps.sample.androiddecompose.utils.consumer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CharactersBlocImplTest {

    private lateinit var bloc: CharactersBloc

    private val lifecycle = LifecycleRegistry().apply { resume() }

    private val outputSubject = PublishSubject<CharactersBloc.Output>()
    private val output = outputSubject.test()

    @Before
    fun setUp() {
        overrideSchedulers(main = { TestScheduler() }, io = { TestScheduler() })

        bloc = CharactersBlocImpl(
            componentContext = DefaultComponentContext(lifecycle),
            storeFactory = DefaultStoreFactory(),
            output = output
        )
    }

    @Test
    fun `query changed should update query text`() {
        val expected = "some cool query"

        bloc.onQueryChanged(expected)

        assertEquals(expected, bloc.models.value.query)
    }

    @Test
    fun `clear query clicked should clear out query`() {
        val expected = "some query"

        bloc.onQueryChanged(expected)
        assertEquals(expected, bloc.models.value.query)

        bloc.onClearQueryClicked()
        assertEquals("", bloc.models.value.query)
    }

    @Test
    fun `on character clicked should emit output of that character`() {
        val expected = Character(1L, "some name", "")

        bloc.onCharacterClicked(expected)

        output.assertValue(CharactersBloc.Output.OpenCharacter(expected))
    }
}