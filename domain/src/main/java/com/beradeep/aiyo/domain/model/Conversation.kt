package com.beradeep.aiyo.domain.model

import java.util.UUID

data class Conversation(val id: UUID, val title: String, val createdAt: Long)
