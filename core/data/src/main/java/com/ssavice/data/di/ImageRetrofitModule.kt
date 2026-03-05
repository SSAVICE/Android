package com.ssavice.data.di

import com.ssavice.network.retrofit.service.ImageUploadService
import com.ssavice.network.di.RetrofitModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageRetrofitModule {
    @Provides
    @Singleton
    fun provideImageRetrofitService(
        @RetrofitModule.ImageRetrofit imageRetrofit: Retrofit,
    ): ImageUploadService =
        imageRetrofit
            .create(ImageUploadService::class.java)
}
