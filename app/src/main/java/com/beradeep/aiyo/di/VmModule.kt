package com.beradeep.aiyo.di

import com.beradeep.aiyo.domain.ApiClient
import com.beradeep.aiyo.domain.repository.ApiKeyRepository
import com.beradeep.aiyo.domain.repository.ChatRepository
import com.beradeep.aiyo.domain.repository.ModelRepository
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
    apiClient: ApiClient
) : ChatViewModel(
    apiKeyRepository = apiKeyRepository,
    chatRepository = chatRepository,
    modelRepository = modelRepository,
    apiClient = apiClient
)

@HiltViewModel
class SettingsViewModelImpl
@Inject
constructor(
    apiKeyRepository: ApiKeyRepository,
    apiClient: ApiClient,
    modelRepository: ModelRepository
) : SettingsViewModel(
    apiKeyRepository = apiKeyRepository,
    modelRepository = modelRepository,
    apiClient = apiClient
)
