package com.beradeep.aiyo.data.remote

import com.aallam.openai.api.http.Timeout
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIHost
import com.beradeep.aiyo.domain.repository.ApiKeyRepository
import kotlin.time.Duration.Companion.seconds

class ApiClientImpl(private val apiKeyRepository: ApiKeyRepository) : DataApiClient {
    private val apiKey get() = apiKeyRepository.getApiKey()

    init { load() }

    @Volatile
    override var openAI: OpenAI? = null

    override fun load() {
        synchronized(this) {
            apiKey?.let { key ->
                openAI =
                    OpenAI(
                        token = key,
                        host = OpenAIHost(DataApiClient.BASE_URL),
                        timeout = Timeout(connect = 30.seconds, socket = 120.seconds)
                    )
            }
        }
    }
}
