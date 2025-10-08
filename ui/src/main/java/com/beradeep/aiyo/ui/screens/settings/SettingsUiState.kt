package com.beradeep.aiyo.ui.screens.settings

import com.beradeep.aiyo.domain.model.Model

data class SettingsUiState(
    val apiKey: String?,
    val models: List<Model>,
    val selectedModel: Model,
    val isFetchingModels: Boolean,
    val showModelSelectionSheet: Boolean
) {
    companion object {
        val defaultModel = Model("openrouter/auto")
        val Default = SettingsUiState(
            apiKey = null,
            models = listOf(defaultModel),
            selectedModel = defaultModel,
            isFetchingModels = false,
            showModelSelectionSheet = false
        )
    }
}
