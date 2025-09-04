package com.beradeep.aiyo.di

import com.beradeep.aiyo.data.remote.ApiClientImpl
import com.beradeep.aiyo.data.remote.DataApiClient
import com.beradeep.aiyo.domain.ApiClient
import com.beradeep.aiyo.domain.repository.ApiKeyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideApiClient(apiKeyRepository: ApiKeyRepository): ApiClientImpl =
        ApiClientImpl(apiKeyRepository)

    @Provides
    @Singleton
    fun provideDataApiClient(apiClientImpl: ApiClientImpl): DataApiClient = apiClientImpl

    @Provides
    @Singleton
    fun provideDomainApiClient(apiClientImpl: ApiClientImpl): ApiClient = apiClientImpl
}
