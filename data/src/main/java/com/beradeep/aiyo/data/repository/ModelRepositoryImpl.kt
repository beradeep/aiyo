package com.beradeep.aiyo.data.repository

import android.content.Context
import com.beradeep.aiyo.data.local.kv.KVStore
import com.beradeep.aiyo.data.local.kv.entity.ModelEntity
import com.beradeep.aiyo.data.remote.DataApiClient
import com.beradeep.aiyo.data.toDomain
import com.beradeep.aiyo.data.toEntity
import com.beradeep.aiyo.data.toModel
import com.beradeep.aiyo.domain.model.Model
import com.beradeep.aiyo.domain.repository.ModelRepository
import kotlinx.serialization.json.Json

class ModelRepositoryImpl(context: Context, val apiClient: DataApiClient) : ModelRepository {
    private val kvStore by lazy { KVStore.getInstance(context) }
    private val json = Json { ignoreUnknownKeys = true }
    private val openAi get() = apiClient.openAI

    override suspend fun getModels(apiKey: String?): Result<List<Model>> = openAi?.let { client ->
        safeCall {
            client.models().map(com.aallam.openai.api.model.Model::toModel)
        }
    } ?: Result.failure(IllegalStateException("OpenAI client not initialized"))

    override fun getDefaultModel(): Model {
        val modelJson = kvStore.getString(KEY_DEFAULT_MODEL)
        return modelJson?.let {
            try {
                json.decodeFromString<ModelEntity>(it).toDomain()
            } catch (_: Throwable) {
                Model.defaultModel
            }
        } ?: Model.defaultModel
    }

    override fun setDefaultModel(model: Model) {
        val modelJson = json.encodeToString(model.toEntity())
        kvStore.putString(KEY_DEFAULT_MODEL, modelJson)
    }

    private suspend fun <T> safeCall(call: suspend () -> T): Result<T> = runCatching { call() }

    companion object {
        private const val KEY_DEFAULT_MODEL = "default_model"
    }
}
