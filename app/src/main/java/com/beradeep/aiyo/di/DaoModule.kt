package com.beradeep.aiyo.di

import com.beradeep.aiyo.data.local.AiyoDatabase
import com.beradeep.aiyo.data.local.dao.ConversationDao
import com.beradeep.aiyo.data.local.dao.MessageDao
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    @Reusable
    fun provideConversationDao(aiyoDatabase: AiyoDatabase): ConversationDao =
        aiyoDatabase.conversationDao()

    @Provides
    @Reusable
    fun provideMessageDao(aiyoDatabase: AiyoDatabase): MessageDao = aiyoDatabase.messageDao()
}
