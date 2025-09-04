package com.beradeep.aiyo.data.remote

import com.aallam.openai.client.OpenAI
import com.beradeep.aiyo.domain.ApiClient

interface DataApiClient : ApiClient {
    val openAI: OpenAI?

    companion object {
        const val BASE_URL = "https://openrouter.ai/api/v1/"
    }
}
