package com.plusmobileapps.sample.androiddecompose.root

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.plusmobileapps.sample.androiddecompose.characters.CharactersBloc
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RootBlocImplTest {

//    private val lifecycle = LifecycleRegistry().apply { resume() }
//
//    private lateinit var bloc: RootBloc
//
//    private val charactersBloc: CharactersBloc = mockk()
//    private lateinit var charactersOutput: (CharactersBloc.Output) -> Unit
//
//    @Before
//    fun setUp() {
//        bloc = RootBlocImpl(
//            componentContext = DefaultComponentContext(lifecycle),
//            characters = { _, output ->
//                charactersOutput = output
//                charactersBloc
//            }
//        )
//    }
//
//    @Test
//    fun `initial child is characters list bloc`() {
//        assertTrue(bloc.activeChild is RootBloc.Child.BottomNav)
//    }
//
//    private val RootBloc.activeChild: RootBloc.Child get() = routerState.value.activeChild.instance
}