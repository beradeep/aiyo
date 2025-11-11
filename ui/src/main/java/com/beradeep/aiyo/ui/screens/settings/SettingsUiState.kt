package com.beradeep.aiyo.ui.screens.settings

import com.beradeep.aiyo.domain.model.Model
import com.beradeep.aiyo.domain.model.ThemeType

data class SettingsUiState(
    val apiKey: String?,
    val models: List<Model>,
    val selectedModel: Model,
    val isFetchingModels: Boolean,
    val showModelSelectionSheet: Boolean,
    val themeType: ThemeType
) {
    companion object {
        val defaultModel = Model.defaultModel
        val Default = SettingsUiState(
            apiKey = null,
            models = listOf(defaultModel),
            selectedModel = defaultModel,
            isFetchingModels = false,
            showModelSelectionSheet = false,
            themeType = ThemeType.System
        )
    }
}
