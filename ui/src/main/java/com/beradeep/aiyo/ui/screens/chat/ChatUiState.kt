package com.beradeep.aiyo.ui.screens.chat

import androidx.compose.runtime.Immutable
import com.beradeep.aiyo.domain.model.Conversation
import com.beradeep.aiyo.domain.model.Model
import com.beradeep.aiyo.domain.model.Reason
import com.beradeep.aiyo.domain.repository.SettingRepository
import com.mikepenz.markdown.model.State

@Immutable
data class ChatUiState(
    val models: List<Model>,
    val selectedModel: Model,
    val favoriteModelIds: Set<String>,
    val selectedConversation: Conversation?,
    val streamingResponse: String?,
    val isLoadingResponse: Boolean,
    val isFetchingModels: Boolean,
    val apiKey: String?,
    val inputText: String,
    val isWebSearchEnabled: Boolean,
    val reasoningEffort: Reason,
    val requestFontSize: Int,
    val responseFontSize: Int,
    val messages: List<UiMessage>,
    val conversations: List<Conversation>,
    val conversationFilter: ConversationFilter
) {
    val isStreamingResponse
        get() = streamingResponse != null

    companion object {
        val defaultModel = Model.defaultModel
        val Default =
            ChatUiState(
                models = listOf(defaultModel),
                selectedModel = defaultModel,
                favoriteModelIds = emptySet(),
                selectedConversation = null,
                streamingResponse = null,
                isLoadingResponse = false,
                isFetchingModels = false,
                apiKey = "apiKey",
                inputText = "",
                isWebSearchEnabled = false,
                reasoningEffort = Reason.None,
                requestFontSize = SettingRepository.DEFAULT_REQUEST_FONT_SIZE,
                responseFontSize = SettingRepository.DEFAULT_RESPONSE_FONT_SIZE,
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
