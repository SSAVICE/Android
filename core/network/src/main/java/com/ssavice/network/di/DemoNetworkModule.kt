package com.ssavice.network.di

import com.ssavice.network.authentication.AuthenticationRepository
import com.ssavice.network.authentication.TokenRepository
import com.ssavice.network.authentication.demo.DemoAuthenticationRepository
import com.ssavice.network.authentication.demo.DemoTokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DemoNetworkModule {
    @Binds
    fun bindTokenRepository(impl: DemoTokenRepository): TokenRepository

    @Binds
    fun bindAuthenticationRepository(impl: DemoAuthenticationRepository): AuthenticationRepository
}
