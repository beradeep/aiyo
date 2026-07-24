package com.beradeep.aiyo.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenRouterCreditsResponse(
    val data: OpenRouterCreditsDto
)

@Serializable
data class OpenRouterCreditsDto(
    @SerialName("total_credits") val totalCredits: Double = 0.0,
    @SerialName("total_usage") val totalUsage: Double = 0.0
)
