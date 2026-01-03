package com.ssavice.data.di

import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.data.repositoryimpl.DemoSellerInfoRepository
import com.ssavice.data.repositoryimpl.RemoteSellerInfoRepository
import com.ssavice.data.service.CompanyRetrofitService
import com.ssavice.network.retrofit.RetrofitModule
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CompanyRetrofitModule {
    @Provides
    @Singleton
    fun provideCompanyRetrofitService(
        @RetrofitModule.ServiceRetrofit serviceRetrofit: Retrofit,
    ): CompanyRetrofitService =
        serviceRetrofit
            .create(CompanyRetrofitService::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class CompanyModule {
    @Binds
    @Singleton
    internal abstract fun bindSellerInfoRepository(remoteSellerInfoRepository: RemoteSellerInfoRepository): SellerInfoRepository
}
