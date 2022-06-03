package com.plusmobileapps.sample.androiddecompose.characters

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.plusmobileapps.sample.androiddecompose.data.RickAndMortyCharacter
import com.plusmobileapps.sample.androiddecompose.util.TestDispatchers
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CharactersBlocImplTest {

    private lateinit var bloc: CharactersBloc

    private val lifecycle = LifecycleRegistry().apply { resume() }

    private val output: (CharactersBloc.Output) -> Unit = mockk(relaxed = true)

    private val dispatchers = TestDispatchers()

    @Before
    fun setUp() {
        bloc = CharactersBlocImpl(
            componentContext = DefaultComponentContext(lifecycle),
            dispatchers = dispatchers,
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
        val expected = RickAndMortyCharacter(1L, "some name", "")

        bloc.onCharacterClicked(expected)

        verify { output.invoke(CharactersBloc.Output.OpenCharacter(expected)) }
    }
}