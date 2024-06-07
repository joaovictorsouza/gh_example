package br.eng.joaovictor.gh.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pull(
    val id: Long,
    val title: String,
    val body: String?,
    val user: Owner,
    val state: String,
    @SerialName("closed_at")
    val closedAt: String?,
    @SerialName("merged_at")
    val mergedAt: String?
)
