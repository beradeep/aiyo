package com.beradeep.aiyo.data

import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.beradeep.aiyo.data.local.db.entity.ConversationEntity
import com.beradeep.aiyo.data.local.db.entity.MessageEntity
import com.beradeep.aiyo.data.local.kv.entity.ModelEntity
import com.beradeep.aiyo.data.remote.OpenRouterModelDto
import com.beradeep.aiyo.domain.model.Conversation
import com.beradeep.aiyo.domain.model.Message
import com.beradeep.aiyo.domain.model.Model
import com.beradeep.aiyo.domain.model.Role
import java.util.Date
import java.util.UUID

fun ChatMessage.toChatMessage(): Message = Message(
    role = this.role.toRole(),
    content = this.content ?: "",
    id = UUID.randomUUID()
)

fun Message.toChatMessage(): ChatMessage = ChatMessage(
    role = role.toChatRole(),
    content = this.content
)

fun ChatRole.toRole(): Role = Role.valueOf(this.role)

fun Role.toChatRole(): ChatRole = when (this) {
    Role.User -> ChatRole.User
    Role.Assistant -> ChatRole.Assistant
    Role.System -> ChatRole.System
}

fun OpenRouterModelDto.toModel(): Model = Model(
    id = id,
    createdAt = created?.let { Date(it * 1000) },
    inputPricePerMillion = pricing?.prompt?.toPricePerMillion(),
    outputPricePerMillion = pricing?.completion?.toPricePerMillion()
)

private fun String.toPricePerMillion(): Double? =
    toDoubleOrNull()?.takeIf { it >= 0 }?.times(1_000_000)

fun ConversationEntity.toDomain(): Conversation = Conversation(
    id = id,
    title = title,
    selectedModel = Model(selectedModel),
    isStarred = isStarred,
    createdAt = createdAt,
    lastUpdatedAt = lastUpdatedAt
)

fun Conversation.toEntity(): ConversationEntity = ConversationEntity(
    id = id,
    title = title,
    isStarred = isStarred,
    createdAt = createdAt,
    lastUpdatedAt = lastUpdatedAt,
    selectedModel = selectedModel.id
)

fun MessageEntity.toDomain(): Message = Message(
    id = id,
    role = Role.valueOf(role),
    content = content
)

fun Message.toEntity(conversationId: UUID): MessageEntity = MessageEntity(
    id = id,
    conversationId = conversationId,
    role = role.name,
    content = content,
    timestamp = System.currentTimeMillis()
)

fun ModelEntity.toDomain(): Model = Model(
    id = id,
    createdAt = createdAt,
    ownedBy = ownedBy,
    inputPricePerMillion = inputPricePerMillion,
    outputPricePerMillion = outputPricePerMillion
)

fun Model.toEntity(): ModelEntity = ModelEntity(
    id = id,
    createdAt = createdAt,
    ownedBy = ownedBy,
    inputPricePerMillion = inputPricePerMillion,
    outputPricePerMillion = outputPricePerMillion
)
