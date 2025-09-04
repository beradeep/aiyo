package com.beradeep.aiyo.domain.repository

interface ApiKeyRepository {
    fun getApiKey(): String?

    fun setApiKey(apiKey: String)
}
