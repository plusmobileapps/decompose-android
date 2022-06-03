package com.plusmobileapps.sample.androiddecompose.data

data class RickAndMortyCharacter(val id: Int, val name: String, val imageUrl: String) {
    companion object {
        fun fromDTO(dto: CharacterDTO): RickAndMortyCharacter =
            RickAndMortyCharacter(
                id = dto.id,
                name = dto.name,
                imageUrl = dto.image
            )
    }
}
