package com.beradeep.aiyo.data.repository

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.Effort
import com.aallam.openai.api.chat.StreamOptions
import com.aallam.openai.api.model.ModelId
import com.beradeep.aiyo.data.local.db.dao.ConversationDao
import com.beradeep.aiyo.data.local.db.dao.MessageDao
import com.beradeep.aiyo.data.remote.DataApiClient
import com.beradeep.aiyo.data.toChatMessage
import com.beradeep.aiyo.data.toDomain
import com.beradeep.aiyo.data.toEntity
import com.beradeep.aiyo.domain.model.Conversation
import com.beradeep.aiyo.domain.model.Message
import com.beradeep.aiyo.domain.model.Model
import com.beradeep.aiyo.domain.model.Reason
import com.beradeep.aiyo.domain.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

class ChatRepositoryImpl(
    private val conversationDao: ConversationDao,
    private val messageDao: MessageDao,
    private val apiClient: DataApiClient
) : ChatRepository {
    private val openAi get() = apiClient.openAI

    override suspend fun getCompletion(
        model: Model,
        message: Message,
        apiKey: String?
    ): Result<String?> = openAi?.let { client ->
        safeCall {
            client
                .chatCompletion(
                    ChatCompletionRequest(
                        model = ModelId(model.id),
                        messages = listOf(message.toChatMessage())
                    )
                ).choices
                .first()
                .message.content
        }
    } ?: Result.failure(IllegalStateException("OpenAI client not initialized"))

    override suspend fun getChatCompletion(
        model: Model,
        messages: List<Message>,
        apiKey: String?
    ): Result<String?> = openAi?.let { client ->
        safeCall {
            client
                .chatCompletion(
                    ChatCompletionRequest(
                        model = ModelId(model.id),
                        messages = messages.map(Message::toChatMessage)
                    )
                ).choices
                .first()
                .message.content
        }
    } ?: Result.failure(IllegalStateException("OpenAI client not initialized"))

    override suspend fun getChatCompletionFlow(
        model: Model,
        messages: List<Message>,
        reason: Reason,
        apiKey: String?
    ): Result<Flow<String>> = openAi?.let { client ->
        safeCall {
            client
                .chatCompletions(
                    ChatCompletionRequest(
                        model = ModelId(model.id),
                        messages = messages.map(Message::toChatMessage),
                        streamOptions = StreamOptions(true),
                        reasoningEffort = reason.effort?.let { Effort(it) }
                    )
                ).mapNotNull { chunk ->
                    chunk.choices
                        .first()
                        .delta
                        ?.content
                }
                .catch { e -> e.message?.let { emit("Error: $it") } }
                .flowOn(Dispatchers.IO)
        }
    } ?: Result.failure(IllegalStateException("OpenAI client not initialized"))

    override suspend fun getConversations(): List<Conversation> =
        conversationDao.getAllConversations().map {
            it.toDomain()
        }

    override fun getConversationsFlow(): Flow<List<Conversation>> {
        return conversationDao.getAllConversationsFlow()
            .map { flow -> flow.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun createConversation(title: String, model: Model, isStarred: Boolean): Conversation {
        val timeStamp = System.currentTimeMillis()
        val conversation =
            Conversation(
                id = java.util.UUID.randomUUID(),
                title = title,
                selectedModel = model,
                isStarred = isStarred,
                createdAt = timeStamp,
                lastUpdatedAt = timeStamp
            )
        conversationDao.upsertConversation(conversation.toEntity())
        return conversation
    }

    override suspend fun updateConversation(conversation: Conversation) {
        conversationDao.upsertConversation(conversation.toEntity())
    }

    override suspend fun deleteConversation(id: java.util.UUID) {
        conversationDao.deleteConversation(id)
        messageDao.deleteMessagesByConversation(id)
    }

    override suspend fun getMessages(conversationId: java.util.UUID) =
        messageDao.getMessagesForConversation(conversationId).map { it.toDomain() }

    override suspend fun insertMessage(message: Message, conversationId: java.util.UUID) {
        messageDao.insertMessage(message.toEntity(conversationId))
    }

    override suspend fun insertMessages(messages: List<Message>, conversationId: java.util.UUID) {
        messageDao.insertMessages(messages.map { it.toEntity(conversationId) })
    }

    override suspend fun deleteMessages(conversationId: java.util.UUID) {
        messageDao.deleteMessagesByConversation(conversationId)
    }

    private suspend fun <T> safeCall(call: suspend () -> T): Result<T> = runCatching { call() }
}
