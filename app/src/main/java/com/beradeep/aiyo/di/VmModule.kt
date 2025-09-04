package com.beradeep.aiyo.di

import com.beradeep.aiyo.domain.ApiClient
import com.beradeep.aiyo.domain.repository.ApiKeyRepository
import com.beradeep.aiyo.domain.repository.ChatRepository
import com.beradeep.aiyo.ui.screens.chat.ChatViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModelImpl
@Inject
constructor(
    apiKeyRepository: ApiKeyRepository,
    chatRepository: ChatRepository,
    apiClient: ApiClient
) : ChatViewModel(
    apiKeyRepository = apiKeyRepository,
    chatRepository = chatRepository,
    apiClient = apiClient
)
