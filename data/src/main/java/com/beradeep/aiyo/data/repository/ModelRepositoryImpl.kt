package com.beradeep.aiyo.data.repository

import android.content.Context
import com.beradeep.aiyo.data.local.kv.KVStore
import com.beradeep.aiyo.data.local.kv.entity.ModelEntity
import com.beradeep.aiyo.data.remote.DataApiClient
import com.beradeep.aiyo.data.remote.OpenRouterModelDto
import com.beradeep.aiyo.data.remote.OpenRouterModelsResponse
import com.beradeep.aiyo.data.toDomain
import com.beradeep.aiyo.data.toEntity
import com.beradeep.aiyo.data.toModel
import com.beradeep.aiyo.domain.model.Model
import com.beradeep.aiyo.domain.repository.ModelRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json

class ModelRepositoryImpl(context: Context, val apiClient: DataApiClient) : ModelRepository {
    private val kvStore by lazy { KVStore.getInstance(context) }
    private val json = Json { ignoreUnknownKeys = true }
    private val httpClient by lazy { HttpClient() }
    private val favoriteModelIdsFlow by lazy { MutableStateFlow(loadFavoriteModelIds()) }

    override suspend fun getModels(apiKey: String?): Result<List<Model>> = safeCall {
        val body = httpClient.get(MODELS_URL).bodyAsText()
        json.decodeFromString<OpenRouterModelsResponse>(body)
            .data
            .map(OpenRouterModelDto::toModel)
    }

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

    override fun getFavoriteModelIdsFlow(): Flow<Set<String>> = favoriteModelIdsFlow

    override fun toggleFavoriteModel(model: Model) {
        val updated = favoriteModelIdsFlow.value.toMutableSet().apply {
            if (!add(model.id)) remove(model.id)
        }
        kvStore.putString(KEY_FAVORITE_MODELS, json.encodeToString(updated.toSet()))
        favoriteModelIdsFlow.value = updated
    }

    private fun loadFavoriteModelIds(): Set<String> =
        kvStore.getString(KEY_FAVORITE_MODELS)?.let {
            try {
                json.decodeFromString<Set<String>>(it)
            } catch (_: Throwable) {
                emptySet()
            }
        } ?: emptySet()

    private suspend fun <T> safeCall(call: suspend () -> T): Result<T> = runCatching { call() }

    companion object {
        private const val KEY_DEFAULT_MODEL = "default_model"
        private const val KEY_FAVORITE_MODELS = "favorite_models"
        private const val MODELS_URL = DataApiClient.BASE_URL + "models"
    }
}
