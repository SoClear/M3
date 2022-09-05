package cool.cmg.m3
import androidx.annotation.Keep

import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName

@Serializable
data class Joke(
    val error: Boolean,
    val category: String,
    val type: String,
    val joke: String,
    val flags: Flags,
    val id: Int,
    val safe: Boolean,
    val lang: String
) {
    @Serializable
    data class Flags(
        val nsfw: Boolean,
        val religious: Boolean,
        val political: Boolean,
        val racist: Boolean,
        val sexist: Boolean,
        val explicit: Boolean
    )
}