package com.ssavice.data.di

import com.ssavice.data.repository.UserInfoRepository
import com.ssavice.data.repositoryimpl.RemoteUserInfoRepository
import com.ssavice.network.retrofit.service.UserInfoRetrofitService
import com.ssavice.network.di.RetrofitModule
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserInfoRetrofitModule {
    @Provides
    @Singleton
    fun provideUserInfoRetrofitService(
        @RetrofitModule.ServiceRetrofit serviceRetrofit: Retrofit,
    ): UserInfoRetrofitService =
        serviceRetrofit
            .create(UserInfoRetrofitService::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class UserInfoModule {
    @Binds
    @Singleton
    internal abstract fun bindUserInfoRepository(remoteUserInfoRepository: RemoteUserInfoRepository): UserInfoRepository
}
