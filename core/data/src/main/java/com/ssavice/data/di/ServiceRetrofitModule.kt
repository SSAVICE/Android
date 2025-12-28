package com.ssavice.data.di

import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.data.repositoryimpl.RemoteSellerInfoRepository
import com.ssavice.data.service.CompanyRetrofitService
import com.ssavice.data.service.ServiceRetrofitService
import com.ssavice.network.retrofit.RetrofitModule
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

    @Provides
    @Singleton
    fun provideServiceRepository(companyRetrofitService: CompanyRetrofitService): SellerInfoRepository =
        RemoteSellerInfoRepository(
            companyRetrofitService,
        )
}
