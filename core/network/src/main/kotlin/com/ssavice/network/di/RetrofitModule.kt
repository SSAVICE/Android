package com.ssavice.network.di

import com.ssavice.core.network.BuildConfig
import com.ssavice.datastore.repository.JwtRepository
import com.ssavice.network.AuthEventManager
import com.ssavice.network.authentication.AuthenticationRepository
import com.ssavice.network.retrofit.AuthInterceptor
import com.ssavice.network.retrofit.ErrorInterceptor
import com.ssavice.network.retrofit.HeaderInterceptor
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

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ImageRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KakaoRestRetrofit

    @Provides
    @Singleton
    @ServiceAuthRetrofit
    fun provideServiceAuthRetrofitBuilder(): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.BACKEND_URL)
            .addConverterFactory(Json.Default.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    @ServiceRetrofit
    fun provideServiceRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.BACKEND_URL)
            .addConverterFactory(Json.Default.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    @ImageRetrofit
    fun provideImageRetrofitBuilder(): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.BACKEND_URL)
            .addConverterFactory(Json.Default.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    @KakaoRestRetrofit
    fun provideKakaoRetrofitBuilder(interceptor: HttpLoggingInterceptor): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.KAKAO_REST_URL)
            .addConverterFactory(Json.Default.asConverterFactory("application/json".toMediaType()))
            .client(
                OkHttpClient
                    .Builder()
                    .addInterceptor(interceptor)
                    .build(),
            ).build()

    @Provides
    @Singleton
    fun provideHeaderInterceptor(tokenRepository: JwtRepository) = HeaderInterceptor(tokenRepository)

    @Provides
    @Singleton
    fun provideErrorInterceptor(eventManager: AuthEventManager) = ErrorInterceptor(eventManager)

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenRepository: JwtRepository,
        authRepository: AuthenticationRepository,
        authEventManager: AuthEventManager,
    ) = AuthInterceptor(tokenRepository, authRepository, authEventManager)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        headerInterceptor: HeaderInterceptor,
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        errorInterceptor: ErrorInterceptor,
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(errorInterceptor)
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
