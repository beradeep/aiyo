package com.beradeep.aiyo.domain.model

import java.util.UUID

data class Message(val id: UUID, val role: Role, val content: String)
