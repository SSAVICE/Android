package com.ssavice.data.di

import com.ssavice.network.di.RetrofitModule
import com.ssavice.network.retrofit.service.KakaoRestService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KakaoRestRetrofitModule {
    @Provides
    @Singleton
    fun provideKakaoRestRetrofitService(
        @RetrofitModule.KakaoRestRetrofit kakaoRestRetrofit: Retrofit,
    ): KakaoRestService =
        kakaoRestRetrofit
            .create(KakaoRestService::class.java)
}
