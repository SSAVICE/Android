package com.ssavice.authentication.di

import com.ssavice.authentication.service.AuthRetrofitService
import com.ssavice.network.di.RetrofitModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthRetrofitModule {
    @Provides
    @Singleton
    fun provideCompanyRetrofitService(
        @RetrofitModule.ServiceAuthRetrofit serviceAuthRetrofit: Retrofit,
    ): AuthRetrofitService =
        serviceAuthRetrofit
            .create(AuthRetrofitService::class.java)
}
