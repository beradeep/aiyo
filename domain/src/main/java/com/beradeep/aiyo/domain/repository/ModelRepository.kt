package com.beradeep.aiyo.domain.repository

import com.beradeep.aiyo.domain.model.Model
import kotlinx.coroutines.flow.Flow

interface ModelRepository {

    suspend fun getModels(apiKey: String? = null): Result<List<Model>>

    fun getDefaultModel(): Model

    fun setDefaultModel(model: Model)

    fun getFavoriteModelIdsFlow(): Flow<Set<String>>

    fun toggleFavoriteModel(model: Model)
}
