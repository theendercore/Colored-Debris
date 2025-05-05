package com.theendercore.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PackFile(val pack: PackSettings)

@Serializable
data class PackSettings(
    val packFormat: Int,
    val description: String,
    @SerialName("supported_formats")
    val supportedFormats: SupportedFormats?,
    val packVersion: String?
) {
    constructor(description: String, maxVersion: Int, packVersion: String) :
            this(15, description, SupportedFormats(15, maxVersion), packVersion)

    fun pack() = PackFile(this)
}

@Serializable
data class SupportedFormats(
    @SerialName("min_inclusive") val minInclusive: Int,
    @SerialName("max_inclusive") val maxInclusive: Int
)