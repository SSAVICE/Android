package com.ssavice.network.retrofit

import com.ssavice.core.network.BuildConfig
import com.ssavice.network.authentication.AuthenticationRepository
import com.ssavice.network.authentication.TokenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ServiceRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ServiceAuthRetrofit

    @Provides
    @Singleton
    @ServiceAuthRetrofit
    fun provideServiceAuthRetrofitBuilder(): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.BACKEND_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    @ServiceRetrofit
    fun provideServiceRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.BACKEND_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideHeaderInterceptor(tokenRepository: TokenRepository) = HeaderInterceptor(tokenRepository)

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenRepository: TokenRepository,
        authRepository: AuthenticationRepository,
    ) = AuthInterceptor(tokenRepository, authRepository)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        headerInterceptor: HeaderInterceptor,
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideLoggingInterceptor() =
        HttpLoggingInterceptor()
            .setLevel(
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                },
            )
}
