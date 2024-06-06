package br.eng.joaovictor.gh.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Repo(
    val id: Long,
    val name: String,
    val description: String?,
    val owner: Owner,
    @SerialName("stargazers_count")
    val stargazersCount: Int,
    @SerialName("forks_count")
    val forksCount: Int
)
