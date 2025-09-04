package com.beradeep.aiyo.ui.screens.chat

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beradeep.aiyo.domain.ApiClient
import com.beradeep.aiyo.domain.model.Conversation
import com.beradeep.aiyo.domain.model.Message
import com.beradeep.aiyo.domain.model.Model
import com.beradeep.aiyo.domain.model.Role
import com.beradeep.aiyo.domain.repository.ApiKeyRepository
import com.beradeep.aiyo.domain.repository.ChatRepository
import com.mikepenz.markdown.model.parseMarkdownFlow
import java.text.DateFormat
import java.util.Date
import java.util.LinkedHashMap
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class ChatViewModel(
    private val apiKeyRepository: ApiKeyRepository,
    private val chatRepository: ChatRepository,
    private val apiClient: ApiClient
) : ViewModel() {
    private val messages = mutableStateListOf<UiMessage>()
    private val conversations = mutableStateListOf<Conversation>()

    private val maxPreload = 10
    private val _uiState = MutableStateFlow(ChatUiState.Default.copy(maxPreload = maxPreload))

    private val messagesFlow = snapshotFlow { messages.toList() }
    private val conversationsFlow = snapshotFlow { conversations.toList() }

    val uiState =
        combine(
            _uiState,
            messagesFlow,
            conversationsFlow
        ) { state, messages, conversations ->
            state.copy(
                messages = messages,
                conversations = conversations
            )
        }.onStart {
            loadApiKey()
            fetchModels()
            loadConversations()
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            ChatUiState.Default.copy(maxPreload = maxPreload)
        )

    private val preloadJobs =
        object : LinkedHashMap<Int, Job>(maxPreload + 1, 0.75f, true) {
            override fun removeEldestEntry(eldest: MutableMap.MutableEntry<Int, Job>?): Boolean =
                if (size > maxPreload) {
                    eldest?.value?.cancel()
                    true
                } else {
                    false
                }
        }

    fun onUiEvent(chatUiEvent: ChatUiEvent) {
        when (chatUiEvent) {
            is ChatUiEvent.OnInputTextEdit -> editInputText(chatUiEvent.str)
            is ChatUiEvent.OnMessageSend -> viewModelScope.launch { sendMessage() }
            is ChatUiEvent.OnSetApiKey -> setApiKey(chatUiEvent.apiKey)
            is ChatUiEvent.OnPreloadMarkdownRequest -> preloadMarkdownForIndex(chatUiEvent.index)
            is ChatUiEvent.OnCancelPreloadMarkdownJobs -> cancelExistingPreloadJobs()
            is ChatUiEvent.OnFetchModels -> viewModelScope.launch { fetchModels() }
            is ChatUiEvent.OnModelSelected -> selectModel(chatUiEvent.model)
            is ChatUiEvent.OnConversationSelected ->
                viewModelScope.launch {
                    selectConversation(
                        chatUiEvent.conversation
                    )
                }

            is ChatUiEvent.OnCreateNewConversation -> viewModelScope.launch {
                createNewConversation()
            }
            is ChatUiEvent.OnLoadConversations -> viewModelScope.launch { loadConversations() }
            ChatUiEvent.OnWebSearchTapped -> _uiState.update {
                it.copy(isWebSearchEnabled = !it.isWebSearchEnabled)
            }
            is ChatUiEvent.OnReason -> _uiState.update {
                it.copy(reasoningEffort = chatUiEvent.reason)
            }
        }
    }

    private fun editInputText(string: String) {
        _uiState.update { it.copy(inputText = string) }
    }

    private fun loadApiKey() {
        _uiState.update { it.copy(apiKey = apiKeyRepository.getApiKey()) }
    }

    private fun setApiKey(key: String) {
        apiKeyRepository.setApiKey(key)
        apiClient.load()
        loadApiKey()
    }

    private suspend fun sendMessage() {
        val text = uiState.value.inputText
        val isLoadingResponse = uiState.value.isLoadingResponse
        if (text.isEmpty() || isLoadingResponse) return

        if (uiState.value.selectedConversationId == null) {
            createNewConversation()
        }

        val message = Message(UUID.randomUUID(), Role.User, text)
        messages.add(message.toUiMessage())
        _uiState.update { it.copy(isLoadingResponse = true) }
        saveMessage(message)

        val apiKey = uiState.value.apiKey
        if (apiKey.isNullOrBlank()) {
            messages.add(
                UiMessage(
                    content = "API key is not set or invalid.",
                    isUser = false,
                    id = UUID.randomUUID().toString()
                )
            )
            return
        }

        _uiState.update { it.copy(inputText = "") }

        _uiState.update { it.copy(isLoadingResponse = true) }
        val model =
            uiState.value.selectedModel.takeIf { !uiState.value.isWebSearchEnabled }
                ?: uiState.value.selectedModel.copy(id = uiState.value.selectedModel.id + ":online")
        val result =
            chatRepository.getChatCompletionFlow(
                apiKey = apiKey,
                model = model,
                messages = messages.map(UiMessage::toMessage)
            )

        result
            .onSuccess { chunkFlow ->
                val response = StringBuilder("")
                chunkFlow
                    .collect { chunk ->
                        response.append(chunk)
                        _uiState.update {
                            it.copy(
                                streamingResponse = response.toString(),
                                isLoadingResponse = false
                            )
                        }
                    }
                _uiState.update { it.copy(streamingResponse = null) }
                val message = Message(UUID.randomUUID(), Role.Assistant, response.toString())
                messages.add(message.toUiMessage())
                saveMessage(message)
                preloadMarkdownForIndex(messages.lastIndex)
            }.onFailure { error ->
                messages.add(
                    UiMessage(
                        isUser = false,
                        content = error.message ?: "Oops. An unknown error occurred.",
                        id = UUID.randomUUID().toString()
                    )
                )
            }
        _uiState.update { it.copy(isLoadingResponse = false) }

        Log.d("ChatViewModel", "Updating conversation title...")
        updateConversationTitle()
    }

    private suspend fun updateConversationTitle() {
        if (messages.size == 2) {
            val prompt =
                Message(
                    id = UUID.randomUUID(),
                    role = Role.User,
                    content = "Summarize the conversation in 3 words."
                )
            val summaryTitle =
                chatRepository.getChatCompletion(
                    apiKey = uiState.value.apiKey,
                    model = Model("mistralai/mistral-nemo:free"),
                    messages = messages.map(UiMessage::toMessage) + prompt
                )
            summaryTitle
                .onSuccess { title ->
                    val conversation =
                        conversations.find {
                            it.id.toString() ==
                                uiState.value.selectedConversationId
                        }
                    conversation?.let {
                        title?.let {
                            chatRepository.updateConversation(
                                conversation.copy(title = title.trim('"'))
                            )
                        }
                    }
                }
        }
    }

    private suspend fun saveMessage(message: Message) {
        uiState.value.selectedConversationId?.let { conversationId ->
            chatRepository.insertMessage(message, UUID.fromString(conversationId))
        }
    }

    private suspend fun fetchModels() {
        val models = chatRepository.getModels(_uiState.value.apiKey)
        models
            .onSuccess { models ->
                _uiState.update { it.copy(models = models) }
            }.onFailure { error ->
                // TODO
            }
    }

    private fun selectModel(model: Model) {
        _uiState.update { it.copy(selectedModel = model) }
    }

    private suspend fun loadConversations() {
        val conversationsUpdated = chatRepository.getConversations()
        conversations.clear()
        conversations.addAll(conversationsUpdated)
    }

    private suspend fun createNewConversation(title: String? = null) {
        cancelExistingPreloadJobs()

        val title = title ?: "New Chat: ${DateFormat.getDateTimeInstance().format(Date())}"
        val conversation = chatRepository.createConversation(title)
        selectConversation(conversation)
        loadConversations()
    }

    private suspend fun selectConversation(conversation: Conversation) {
        // Cancel existing preload jobs before switching conversations
        cancelExistingPreloadJobs()

        _uiState.update { it.copy(selectedConversationId = conversation.id.toString()) }
        messages.clear()
        messages.addAll(chatRepository.getMessages(conversation.id).map { it.toUiMessage() })

        // Preload markdown for initially visible messages
        val initialPreloadCount = minOf(maxPreload, messages.size)
        for (i in 0 until initialPreloadCount) {
            preloadMarkdownForIndex(i)
        }
    }

    private suspend fun deleteConversation(conversation: Conversation) {
        chatRepository.deleteMessages(conversation.id)
        chatRepository.deleteConversation(conversation.id)
        loadConversations()
        messages.clear()
    }

    private fun parseMarkdownForIndex(index: Int): Job? {
        val message = messages.getOrNull(index) ?: return null
        if (message.content.isNullOrBlank() || message.isUser) return null

        return viewModelScope.launch(Dispatchers.Default) {
            try {
                parseMarkdownFlow(message.content).distinctUntilChanged().collectLatest { state ->
                    // Check if the message still exists at this index and hasn't changed
                    val currentMessage = messages.getOrNull(index)
                    if (currentMessage?.id == message.id && currentMessage.markdownState != state) {
                        messages[index] = currentMessage.copy(markdownState = state)
                    }
                }
            } catch (e: Exception) {
                Log.w("ChatViewModel", "Error parsing markdown for index $index", e)
            }
        }
    }

    private fun preloadMarkdownForIndex(index: Int) {
        // Only create new preload job if one doesn't exist for this index
        if (preloadJobs[index] == null) {
            val preloadJob = parseMarkdownForIndex(index)
            preloadJob?.let { preloadJobs[index] = it }
        }
    }

    private fun cancelExistingPreloadJobs() {
        preloadJobs.forEach { (_, job) -> job.cancel() }
        preloadJobs.clear()
    }
}
