package com.ssavice.authentication.di

import com.ssavice.authentication.event.NetworkEventManagerImpl
import com.ssavice.authentication.repositoryimpl.remote.RemoteAuthenticationRepository
import com.ssavice.network.NetworkEventManager
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
    fun bindAuthEventManager(impl: NetworkEventManagerImpl): NetworkEventManager
}
