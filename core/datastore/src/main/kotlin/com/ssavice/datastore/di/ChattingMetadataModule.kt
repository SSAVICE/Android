package com.ssavice.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.google.crypto.tink.Aead
import com.ssavice.datastore.preferences.JwtPreferences
import com.ssavice.datastore.preferences.JwtPreferencesSerializer
import com.ssavice.datastore.preferences.ServiceMetadataPreferences
import com.ssavice.datastore.preferences.ServiceMetadataPreferencesSerializer
import com.ssavice.datastore.preferences.UserInfoPreferences
import com.ssavice.datastore.preferences.UserInfoPreferencesSerializer
import com.ssavice.datastore.repository.ChattingMetadataRepository
import com.ssavice.datastore.repository.JwtRepository
import com.ssavice.datastore.repositoryimpl.LocalChattingMetadataRepository
import com.ssavice.datastore.repositoryimpl.LocalJwtRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChattingMetadataModule {
    @Binds
    @Singleton
    internal abstract fun bindChattingMetadataRepository
                (localChattingMetadataRepository: LocalChattingMetadataRepository): ChattingMetadataRepository
}

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {
    @Provides
    @Singleton
    fun provideServiceMetadataPreference(
        @ApplicationContext context: Context,
    ): DataStore<ServiceMetadataPreferences> =
        DataStoreFactory.create(
            serializer = ServiceMetadataPreferencesSerializer,
            produceFile = { context.dataStoreFile("service_meta_preferences.pb") },
        )

    @Provides
    @Singleton
    fun provideUserInfoPreference(
        @ApplicationContext context: Context,
    ): DataStore<UserInfoPreferences> =
        DataStoreFactory.create(
            serializer = UserInfoPreferencesSerializer,
            produceFile = { context.dataStoreFile("user_info_preferences.pb") },
        )
}
