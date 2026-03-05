package com.ssavice.data.di

import com.ssavice.data.repository.ServiceRepository
import com.ssavice.data.repositoryimpl.RemoteServiceRepository
import com.ssavice.network.retrofit.service.ServiceRetrofitService
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
object ServiceRetrofitModule {
    @Provides
    @Singleton
    fun provideServiceRetrofitService(
        @RetrofitModule.ServiceRetrofit serviceRetrofit: Retrofit,
    ): ServiceRetrofitService =
        serviceRetrofit
            .create(ServiceRetrofitService::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    @Singleton
    internal abstract fun bindServiceRepository(remoteServiceRepository: RemoteServiceRepository): ServiceRepository
}
