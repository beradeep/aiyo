package com.beradeep.aiyo.data.repository

import android.util.Log
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.Effort
import com.aallam.openai.api.chat.StreamOptions
import com.aallam.openai.api.model.ModelId
import com.beradeep.aiyo.data.local.dao.ConversationDao
import com.beradeep.aiyo.data.local.dao.MessageDao
import com.beradeep.aiyo.data.remote.DataApiClient
import com.beradeep.aiyo.data.toChatMessage
import com.beradeep.aiyo.data.toDomain
import com.beradeep.aiyo.data.toEntity
import com.beradeep.aiyo.data.toModel
import com.beradeep.aiyo.domain.model.Conversation
import com.beradeep.aiyo.domain.model.Message
import com.beradeep.aiyo.domain.model.Model
import com.beradeep.aiyo.domain.model.Reason
import com.beradeep.aiyo.domain.repository.ChatRepository
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull

class ChatRepositoryImpl(
    private val conversationDao: ConversationDao,
    private val messageDao: MessageDao,
    private val apiClient: DataApiClient
) : ChatRepository {
    private val openAi get() = apiClient.openAI

    override suspend fun getModels(apiKey: String?): Result<List<Model>> = openAi?.let { client ->
        safeCall {
            client.models().map(com.aallam.openai.api.model.Model::toModel)
        }
    } ?: Result.failure(IllegalStateException("OpenAI client not initialized"))

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
                }.flowOn(Dispatchers.IO)
        }
    } ?: Result.failure(IllegalStateException("OpenAI client not initialized"))

    override suspend fun getConversations(): List<Conversation> =
        conversationDao.getAllConversations().map {
            it.toDomain()
        }

    override suspend fun createConversation(title: String): Conversation {
        val conversation =
            Conversation(
                id = java.util.UUID.randomUUID(),
                title = title,
                createdAt = System.currentTimeMillis()
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

    suspend fun <T> safeCall(call: suspend () -> T): Result<T> = try {
        Result.success(call())
    } catch (e: UnknownHostException) {
        Log.e("ChatRepositoryImpl", "Network error - no internet connection: ${e.message}")
        Result.failure(Exception("No internet connection. Please check your network."))
    } catch (e: SocketTimeoutException) {
        Log.e("ChatRepositoryImpl", "Request timeout: ${e.message}")
        Result.failure(Exception("Request timed out. Please try again."))
    } catch (e: ConnectException) {
        Log.e("ChatRepositoryImpl", "Connection failed: ${e.message}")
        Result.failure(Exception("Failed to connect to the server. Please try again."))
    } catch (e: SSLException) {
        Log.e("ChatRepositoryImpl", "SSL/TLS error: ${e.message}")
        Result.failure(
            Exception("Secure connection failed. Please check your network settings.")
        )
    } catch (e: IOException) {
        Log.e("ChatRepositoryImpl", "Network I/O error: ${e.message}")
        Result.failure(Exception("Network error occurred. Please check your connection."))
    } catch (e: TimeoutCancellationException) {
        Log.e("ChatRepositoryImpl", "Operation cancelled due to timeout: ${e.message}")
        Result.failure(Exception("Operation timed out. Please try again."))
    } catch (e: IllegalArgumentException) {
        Log.e("ChatRepositoryImpl", "Invalid request parameters: ${e.message}")
        Result.failure(Exception("Invalid request. Please check your input."))
    } catch (e: Exception) {
        Log.e("ChatRepositoryImpl", "Error getting streaming completion: ${e.message}", e)
        when {
            e.message?.contains("401") == true ->
                Result.failure(Exception("Invalid API key. Please check your credentials."))

            e.message?.contains("403") == true ->
                Result.failure(
                    Exception("Access forbidden. Please check your API key permissions.")
                )

            e.message?.contains("429") == true ->
                Result.failure(Exception("Rate limit exceeded. Please try again later."))

            e.message?.contains("500") == true ->
                Result.failure(Exception("Server error. Please try again later."))

            e.message?.contains("502") == true || e.message?.contains("503") == true ->
                Result.failure(
                    Exception("Service temporarily unavailable. Please try again later.")
                )

            else -> Result.failure(Exception("An unexpected error occurred: ${e.message}"))
        }
    }
}
