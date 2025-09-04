package com.beradeep.aiyo.di

import android.content.Context
import com.beradeep.aiyo.data.local.dao.ConversationDao
import com.beradeep.aiyo.data.local.dao.MessageDao
import com.beradeep.aiyo.data.remote.DataApiClient
import com.beradeep.aiyo.data.repository.ApiKeyRepositoryImpl
import com.beradeep.aiyo.data.repository.ChatRepositoryImpl
import com.beradeep.aiyo.domain.repository.ApiKeyRepository
import com.beradeep.aiyo.domain.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideApiKeyRepository(@ApplicationContext context: Context): ApiKeyRepository =
        ApiKeyRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideChatRepository(
        conversationDao: ConversationDao,
        messageDao: MessageDao,
        apiClient: DataApiClient
    ): ChatRepository = ChatRepositoryImpl(conversationDao, messageDao, apiClient)
}
