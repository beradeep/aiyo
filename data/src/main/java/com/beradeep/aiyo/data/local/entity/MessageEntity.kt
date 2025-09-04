package com.beradeep.aiyo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: UUID,
    val conversationId: UUID,
    val role: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
