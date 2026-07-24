package com.beradeep.aiyo.di

import com.beradeep.aiyo.domain.ApiClient
import com.beradeep.aiyo.domain.repository.AccountRepository
import com.beradeep.aiyo.domain.repository.ApiKeyRepository
import com.beradeep.aiyo.domain.repository.ChatRepository
import com.beradeep.aiyo.domain.repository.ModelRepository
import com.beradeep.aiyo.domain.repository.SettingRepository
import com.beradeep.aiyo.ui.screens.chat.ChatViewModel
import com.beradeep.aiyo.ui.screens.settings.SettingsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModelImpl
@Inject
constructor(
    apiKeyRepository: ApiKeyRepository,
    chatRepository: ChatRepository,
    modelRepository: ModelRepository,
    settingRepository: SettingRepository,
    apiClient: ApiClient
) : ChatViewModel(
    apiKeyRepository = apiKeyRepository,
    chatRepository = chatRepository,
    modelRepository = modelRepository,
    settingRepository = settingRepository,
    apiClient = apiClient
)

@HiltViewModel
class SettingsViewModelImpl
@Inject
constructor(
    apiKeyRepository: ApiKeyRepository,
    apiClient: ApiClient,
    modelRepository: ModelRepository,
    settingRepository: SettingRepository,
    accountRepository: AccountRepository
) : SettingsViewModel(
    apiKeyRepository = apiKeyRepository,
    modelRepository = modelRepository,
    settingsRepository = settingRepository,
    accountRepository = accountRepository,
    apiClient = apiClient
)
