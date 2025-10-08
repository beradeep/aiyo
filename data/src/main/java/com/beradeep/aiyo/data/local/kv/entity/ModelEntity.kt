package com.beradeep.aiyo.data.local.kv.entity

import com.beradeep.aiyo.data.local.kv.serializer.DateSerializer
import kotlinx.serialization.Serializable

@Serializable
data class ModelEntity(
    val id: String,
    val ownedBy: String? = null,
    @Serializable(with = DateSerializer::class)
    val createdAt: java.util.Date? = null
)
