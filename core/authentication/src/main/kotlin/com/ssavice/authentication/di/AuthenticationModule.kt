package com.ssavice.authentication.di

import com.ssavice.authentication.event.AuthEventManagerImpl
import com.ssavice.authentication.repositoryimpl.remote.RemoteAuthenticationRepository
import com.ssavice.network.AuthEventManager
import com.ssavice.network.authentication.AuthenticationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface AuthenticationModule {
    @Binds
    @Singleton
    fun bindAuthenticationRepository(impl: RemoteAuthenticationRepository): AuthenticationRepository

    @Binds
    @Singleton
    fun bindAuthEventManager(impl: AuthEventManagerImpl): AuthEventManager
}
