package com.beradeep.aiyo.ui.screens.chat

import com.beradeep.aiyo.domain.model.Message
import com.beradeep.aiyo.domain.model.Role
import java.util.UUID

fun Message.toUiMessage() = UiMessage(
    content = content,
    isUser = role == Role.User,
    id = id.toString()
)

fun UiMessage.toMessage(): Message = Message(
    role = if (isUser) Role.User else Role.Assistant,
    content = content ?: "",
    id = UUID.fromString(id)
)
