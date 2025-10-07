package com.beradeep.aiyo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey val id: UUID,
    val title: String,
    val selectedModel: String,
    val isStarred: Boolean,
    val lastUpdatedAt: Long,
    val createdAt: Long
)
