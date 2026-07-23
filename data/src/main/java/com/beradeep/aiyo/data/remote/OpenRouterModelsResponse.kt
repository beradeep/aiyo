package com.beradeep.aiyo.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class OpenRouterModelsResponse(
    val data: List<OpenRouterModelDto>
)

@Serializable
data class OpenRouterModelDto(
    val id: String,
    val created: Long? = null,
    val pricing: OpenRouterPricingDto? = null
)

@Serializable
data class OpenRouterPricingDto(
    val prompt: String? = null,
    val completion: String? = null
)
