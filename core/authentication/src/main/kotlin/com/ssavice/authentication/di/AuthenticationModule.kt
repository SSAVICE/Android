package com.ssavice.authentication.di

import com.ssavice.authentication.repositoryimpl.demo.DemoAuthenticationRepository
import com.ssavice.network.authentication.AuthenticationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface AuthenticationModule {
    @Binds
    fun bindAuthenticationRepository(impl: DemoAuthenticationRepository): AuthenticationRepository
}
