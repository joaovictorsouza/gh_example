package br.eng.joaovictor.gh.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Owner(
    val id: Long,
    val login: String,
    @SerialName("avatar_url")
    val avatarUrl: String
)
