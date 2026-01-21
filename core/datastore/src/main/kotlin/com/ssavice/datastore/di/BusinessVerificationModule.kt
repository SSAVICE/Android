package com.ssavice.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.google.crypto.tink.Aead
import com.ssavice.datastore.preferences.BusinessVerificationPreferences
import com.ssavice.datastore.preferences.BusinessVerificationPreferencesSerializer
import com.ssavice.datastore.repository.BusinessVerificationRepository
import com.ssavice.datastore.repositoryimpl.LocalBusinessVerificationRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CompanyModule {
    @Binds
    @Singleton
    internal abstract fun bindBusinessVerificationRepository(
        localBusinessVerificationRepository: LocalBusinessVerificationRepository,
    ): BusinessVerificationRepository
}

@Module
@InstallIn(SingletonComponent::class)
object CompanyPreferencesModule {
    @Provides
    @Singleton
    fun provideBusinessVerificationPreferencesDataStore(
        @ApplicationContext context: Context,
        aead: Aead,
    ): DataStore<BusinessVerificationPreferences> =
        DataStoreFactory.create(
            serializer = BusinessVerificationPreferencesSerializer(aead),
            produceFile = { context.dataStoreFile("business_verification_preferences.pb") },
        )
}
