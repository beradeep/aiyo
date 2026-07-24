package com.beradeep.aiyo.data.repository

import com.beradeep.aiyo.data.remote.DataApiClient
import com.beradeep.aiyo.data.remote.OpenRouterCreditsResponse
import com.beradeep.aiyo.domain.model.Credits
import com.beradeep.aiyo.domain.repository.AccountRepository
import com.beradeep.aiyo.domain.repository.ApiKeyRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json

class AccountRepositoryImpl(
    private val apiKeyRepository: ApiKeyRepository
) : AccountRepository {
    private val json = Json { ignoreUnknownKeys = true }
    private val httpClient by lazy { HttpClient() }

    override suspend fun getCredits(): Result<Credits> = runCatching {
        val apiKey = apiKeyRepository.getApiKey()
        check(!apiKey.isNullOrBlank()) { "API key is not set" }
        val body = httpClient.get(CREDITS_URL) {
            header(HttpHeaders.Authorization, "Bearer $apiKey")
        }.bodyAsText()
        val credits = json.decodeFromString<OpenRouterCreditsResponse>(body).data
        Credits(
            totalCredits = credits.totalCredits,
            totalUsage = credits.totalUsage
        )
    }

    companion object {
        private const val CREDITS_URL = DataApiClient.BASE_URL + "credits"
    }
}
