package com.beradeep.aiyo.data.repository

import android.content.Context
import com.beradeep.aiyo.data.local.kv.KVStore
import com.beradeep.aiyo.domain.repository.ApiKeyRepository

class ApiKeyRepositoryImpl(context: Context) : ApiKeyRepository {
    private val kvStore by lazy { KVStore.getInstance(context) }

    override fun setApiKey(apiKey: String) {
        kvStore.putEncryptedString(KEY_API, apiKey)
    }

    override fun getApiKey(): String? = kvStore.getEncryptedString(KEY_API)

    companion object {
        private const val KEY_API = "openrouter_api_key"
    }
}
