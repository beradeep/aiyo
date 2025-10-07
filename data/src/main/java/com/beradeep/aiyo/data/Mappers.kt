package com.beradeep.aiyo.data

import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.beradeep.aiyo.data.local.entity.ConversationEntity
import com.beradeep.aiyo.data.local.entity.MessageEntity
import com.beradeep.aiyo.domain.model.Conversation
import com.beradeep.aiyo.domain.model.Message
import com.beradeep.aiyo.domain.model.Model
import com.beradeep.aiyo.domain.model.Role
import java.util.Date
import java.util.UUID
import com.aallam.openai.api.model.Model as OpenAIModel

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

fun OpenAIModel.toModel(): Model = Model(
    id = this.id.id,
    createdAt = this.created?.let { Date(it) },
    ownedBy = this.ownedBy
)

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
