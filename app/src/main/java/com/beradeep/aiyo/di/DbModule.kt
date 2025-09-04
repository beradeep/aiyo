package com.beradeep.aiyo.di

import android.content.Context
import androidx.room.Room
import com.beradeep.aiyo.data.local.AiyoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {
    @Provides
    @Singleton
    fun provideAiyoDatabase(@ApplicationContext context: Context): AiyoDatabase = Room
        .databaseBuilder(context, AiyoDatabase::class.java, "aiyo.db")
        .build()
}
