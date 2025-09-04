package com.beradeep.aiyo.data.local

import android.content.Context
import com.tencent.mmkv.MMKV
import kotlin.getValue

class KVStore(context: Context) {
    private val mmkv by lazy { MMKV.defaultMMKV() }
    private val mmkvEC by lazy {
        MMKV.mmkvWithID("encrypted_mmkv", MMKV.SINGLE_PROCESS_MODE, "aiyo")
    }

    init {
        MMKV.initialize(context)
    }

    fun putString(key: String, value: String) {
        mmkv.encode(key, value)
    }

    fun putBoolean(key: String, value: Boolean) {
        mmkv.encode(key, value)
    }

    fun putLong(key: String, value: Long) {
        mmkv.encode(key, value)
    }

    fun putDouble(key: String, value: Double) {
        mmkv.encode(key, value)
    }

    fun putBytes(key: String, value: ByteArray) {
        mmkv.encode(key, value)
    }

    fun putEncryptedString(key: String, value: String) {
        mmkvEC.encode(key, value)
    }

    fun getString(key: String): String? = mmkv.decodeString(key)

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean =
        mmkv.decodeBool(key, defaultValue)

    fun getLong(key: String, defaultValue: Long = 0L): Long = mmkv.decodeLong(key, defaultValue)

    fun getDouble(key: String, defaultValue: Double = 0.0): Double =
        mmkv.decodeDouble(key, defaultValue)

    fun getBytes(key: String): ByteArray? = mmkv.decodeBytes(key)

    fun getEncryptedString(key: String): String? = mmkvEC.decodeString(key)

    companion object {
        @Volatile
        private var kv: KVStore? = null

        fun getInstance(context: Context): KVStore = kv ?: synchronized(this) {
            KVStore(context).also { kv = it }
        }
    }
}
