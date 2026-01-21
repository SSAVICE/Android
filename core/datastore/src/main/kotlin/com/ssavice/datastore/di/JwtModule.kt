package com.ssavice.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.google.crypto.tink.Aead
import com.ssavice.datastore.preferences.JwtPreferences
import com.ssavice.datastore.preferences.JwtPreferencesSerializer
import com.ssavice.datastore.repository.JwtRepository
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
abstract class JwtModule {
    @Binds
    @Singleton
    internal abstract fun bindJwtRepository(
        localJwtRepository: LocalJwtRepository,
    ): JwtRepository
}

@Module
@InstallIn(SingletonComponent::class)
object JwtPreferencesModule {
    @Provides
    @Singleton
    fun provideJwtPreferencesDataStore(
        @ApplicationContext context: Context,
        aead: Aead,
    ): DataStore<JwtPreferences> =
        DataStoreFactory.create(
            serializer = JwtPreferencesSerializer(aead),
            produceFile = { context.dataStoreFile("jwt_preferences.pb") },
        )
}
