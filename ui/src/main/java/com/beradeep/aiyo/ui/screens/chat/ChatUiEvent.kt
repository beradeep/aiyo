package com.beradeep.aiyo.ui.screens.chat

import com.beradeep.aiyo.domain.model.Conversation
import com.beradeep.aiyo.domain.model.Model
import com.beradeep.aiyo.domain.model.Reason

sealed interface ChatUiEvent {
    data object OnMessageSend : ChatUiEvent

    data class OnInputTextEdit(val str: String) : ChatUiEvent

    data class OnSetApiKey(val apiKey: String) : ChatUiEvent

    data class OnPreloadMarkdownRequest(val index: Int) : ChatUiEvent

    data object OnCancelPreloadMarkdownJobs : ChatUiEvent

    data class OnModelSelected(val model: Model) : ChatUiEvent

    data object OnFetchModels : ChatUiEvent

    data object OnNewChat : ChatUiEvent

    data class OnConversationSelected(val conversation: Conversation) : ChatUiEvent

    data object OnWebSearchTapped : ChatUiEvent

    data class OnUpdateConversation(val conversation: Conversation) : ChatUiEvent

    data class OnDeleteConversation(val conversation: Conversation) : ChatUiEvent

    data class OnReason(val reason: Reason) : ChatUiEvent

    data class OnConversationFilterSelected(val filter: ConversationFilter) : ChatUiEvent

    data object OnStopRequest : ChatUiEvent
}
