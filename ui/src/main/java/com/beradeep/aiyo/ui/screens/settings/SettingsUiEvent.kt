package com.beradeep.aiyo.ui.screens.settings

import com.beradeep.aiyo.domain.model.ThemeType

sealed class SettingsUiEvent {
    data class OnSetApiKey(val apiKey: String) : SettingsUiEvent()
    data class OnModelSelected(val model: com.beradeep.aiyo.domain.model.Model) : SettingsUiEvent()
    object OnFetchModels : SettingsUiEvent()
    object OnShowModelSelectionSheet : SettingsUiEvent()
    object OnDismissModelSelectionSheet : SettingsUiEvent()

    data class OnUpdateThemeType(val themeType: ThemeType) : SettingsUiEvent()
}
