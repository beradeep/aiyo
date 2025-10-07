package com.beradeep.aiyo.domain.model

import java.util.UUID

data class Conversation(
    val id: UUID,
    val title: String,
    val selectedModel: Model,
    val isStarred: Boolean,
    val createdAt: Long,
    val lastUpdatedAt: Long
)
