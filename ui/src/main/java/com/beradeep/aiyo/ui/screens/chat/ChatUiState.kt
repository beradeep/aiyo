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
    val selectedConversationId: String?,
    val streamingResponse: String?,
    val isLoadingResponse: Boolean,
    val apiKey: String?,
    val inputText: String,
    val maxPreload: Int,
    val isWebSearchEnabled: Boolean,
    val reasoningEffort: Reason,
    val messages: List<UiMessage>,
    val conversations: List<Conversation>
) {
    val isStreamingResponse
        get() = streamingResponse != null

    companion object {
        val defaultModel = Model("openrouter/auto")
        val Default =
            ChatUiState(
                models = listOf(defaultModel),
                selectedModel = defaultModel,
                selectedConversationId = null,
                streamingResponse = null,
                isLoadingResponse = false,
                apiKey = "apiKey",
                inputText = "",
                maxPreload = 3,
                isWebSearchEnabled = false,
                reasoningEffort = Reason.None,
                messages = emptyList(),
                conversations = emptyList()
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
