package com.ssavice.data.di

import com.ssavice.data.repository.UserInfoRepository
import com.ssavice.data.repositoryimpl.DemoUserInfoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserInfoModule {
    @Binds
    @Singleton
    internal abstract fun bindUserInfoRepository(demoUserInfoRepository: DemoUserInfoRepository): UserInfoRepository
}
