package com.beradeep.aiyo.data.repository

import android.content.Context
import com.beradeep.aiyo.data.local.kv.KVStore
import com.beradeep.aiyo.domain.model.ThemeType
import com.beradeep.aiyo.domain.repository.SettingRepository

class SettingRepositoryImpl(context: Context) : SettingRepository {

    private val kvStore by lazy { KVStore.getInstance(context) }

    override suspend fun getThemeType(): ThemeType {
        return kvStore.getString(KEY_THEME_TYPE)?.let { ThemeType.valueOf(it) } ?: ThemeType.System
    }

    override suspend fun setThemeType(themeType: ThemeType) {
        return kvStore.putString(KEY_THEME_TYPE, themeType.name)
    }

    companion object {
        const val KEY_THEME_TYPE = "theme_type"
    }
}