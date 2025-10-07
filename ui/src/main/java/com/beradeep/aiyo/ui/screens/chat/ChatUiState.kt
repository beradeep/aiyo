package com.beradeep.aiyo.ui.screens.chat

import androidx.compose.runtime.Immutable
import com.beradeep.aiyo.domain.model.Conversation
import com.beradeep.aiyo.domain.model.Model
import com.beradeep.aiyo.domain.model.Reason
import com.mikepenz.markdown.model.State

@Immutable
data class ChatUiState(
    val models: List<Model>,
    val selectedModel: Model,
    val selectedConversation: Conversation?,
    val streamingResponse: String?,
    val isLoadingResponse: Boolean,
    val isFetchingModels: Boolean,
    val apiKey: String?,
    val inputText: String,
    val isWebSearchEnabled: Boolean,
    val reasoningEffort: Reason,
    val messages: List<UiMessage>,
    val conversations: List<Conversation>,
    val conversationFilter: ConversationFilter
) {
    val isStreamingResponse
        get() = streamingResponse != null

    companion object {
        val defaultModel = Model("openrouter/auto")
        val Default =
            ChatUiState(
                models = listOf(defaultModel),
                selectedModel = defaultModel,
                selectedConversation = null,
                streamingResponse = null,
                isLoadingResponse = false,
                isFetchingModels = false,
                apiKey = "apiKey",
                inputText = "",
                isWebSearchEnabled = false,
                reasoningEffort = Reason.None,
                messages = emptyList(),
                conversations = emptyList(),
                conversationFilter = ConversationFilter.RECENT
            )
    }
}

@Immutable
data class UiMessage(
    val id: String,
    val content: String?,
    val isUser: Boolean,
    val markdownState: State = State.Loading()
)
