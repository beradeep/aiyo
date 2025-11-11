package com.beradeep.aiyo.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beradeep.aiyo.domain.ApiClient
import com.beradeep.aiyo.domain.model.Model
import com.beradeep.aiyo.domain.model.ThemeType
import com.beradeep.aiyo.domain.repository.ApiKeyRepository
import com.beradeep.aiyo.domain.repository.ModelRepository
import com.beradeep.aiyo.domain.repository.SettingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class SettingsViewModel(
    private val apiKeyRepository: ApiKeyRepository,
    private val modelRepository: ModelRepository,
    private val settingsRepository: SettingRepository,
    private val apiClient: ApiClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState.Default)

    val uiState = _uiState
        .onStart { loadInitialState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            SettingsUiState.Default
        )

    fun onUiEvent(settingsUiEvent: SettingsUiEvent) {
        when (settingsUiEvent) {
            is SettingsUiEvent.OnSetApiKey -> setApiKey(settingsUiEvent.apiKey)
            is SettingsUiEvent.OnModelSelected -> selectModel(settingsUiEvent.model)
            SettingsUiEvent.OnFetchModels -> fetchModels()
            SettingsUiEvent.OnShowModelSelectionSheet -> showModelSelectionSheet()
            SettingsUiEvent.OnDismissModelSelectionSheet -> dismissModelSelectionSheet()
            is SettingsUiEvent.OnUpdateThemeType -> setThemeType(settingsUiEvent.themeType)
        }
    }

    private fun loadInitialState() {
        loadThemeType()
        loadApiKey()
        loadDefaultModel()
        fetchModels()
    }

    private fun loadThemeType() {
        viewModelScope.launch {
            _uiState.update { it.copy(themeType = settingsRepository.getThemeType()) }
        }
    }

    private fun loadApiKey() {
        viewModelScope.launch {
            _uiState.update { it.copy(apiKey = apiKeyRepository.getApiKey()) }
        }
    }

    private fun loadDefaultModel() {
        viewModelScope.launch {
            val savedModel = modelRepository.getDefaultModel()
            _uiState.update { it.copy(selectedModel = savedModel) }
        }
    }

    private fun setThemeType(themeType: ThemeType) {
        viewModelScope.launch {
            settingsRepository.setThemeType(themeType)
            _uiState.update { it.copy(themeType = themeType) }
        }
    }

    private fun setApiKey(key: String) {
        apiKeyRepository.setApiKey(key)
        apiClient.load()
        loadApiKey()
    }

    private fun fetchModels() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isFetchingModels = true) }
            modelRepository
                .getModels(_uiState.value.apiKey)
                .onSuccess { models ->
                    _uiState.update { it.copy(models = models) }
                    // Update selected model if it's not set or not in the list
                    val currentSelected = _uiState.value.selectedModel
                    val newSelected = if (!models.contains(currentSelected)) {
                        models.firstOrNull() ?: currentSelected
                    } else {
                        currentSelected
                    }
                    modelRepository.setDefaultModel(newSelected)
                    loadDefaultModel()
                }
            _uiState.update { it.copy(isFetchingModels = false) }
        }
    }

    private fun selectModel(model: Model) {
        _uiState.update { it.copy(selectedModel = model) }
        modelRepository.setDefaultModel(model)
        dismissModelSelectionSheet()
    }

    private fun showModelSelectionSheet() {
        _uiState.update { it.copy(showModelSelectionSheet = true) }
    }

    private fun dismissModelSelectionSheet() {
        _uiState.update { it.copy(showModelSelectionSheet = false) }
    }
}
