package com.plusmobileapps.sample.androiddecompose.character

import com.arkivanov.decompose.value.Value

interface CharacterBloc {

    val models: Value<Model>

    fun onBackClicked()

    data class Model(
        val isLoading: Boolean = true,
        val name: String = "",
        val status: String = "",
        val species: String = "",
        val image: String = ""
    )

    sealed class Output {
        object Finished : Output()
    }
}