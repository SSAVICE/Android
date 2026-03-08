package com.ssavice.network.di

import com.ssavice.core.network.BuildConfig
import com.ssavice.datastore.repository.JwtRepository
import com.ssavice.network.NetworkEventManager
import com.ssavice.network.authentication.AuthenticationRepository
import com.ssavice.network.retrofit.AuthInterceptor
import com.ssavice.network.retrofit.ErrorInterceptor
import com.ssavice.network.retrofit.HeaderInterceptor
import com.ssavice.network.retrofit.adapter.ResultCallAdapterFactory
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
    private val retroJson = Json { ignoreUnknownKeys = true }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ServiceRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ServiceAuthRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ChatRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ImageRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KakaoRestRetrofit

    @Provides
    @Singleton
    fun provideResultCallAdaptorFactory(networkEventManager: NetworkEventManager): ResultCallAdapterFactory =
        ResultCallAdapterFactory(
            networkEventManager,
        )

    @Provides
    @Singleton
    fun provideRetrofitBuilder(callAdapterFactory: ResultCallAdapterFactory): Retrofit.Builder =
        Retrofit
            .Builder()
            .addConverterFactory(retroJson.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(callAdapterFactory)

    @Provides
    @Singleton
    @ServiceAuthRetrofit
    fun provideServiceAuthRetrofitBuilder(builder: Retrofit.Builder): Retrofit =
        builder
            .baseUrl(BuildConfig.BACKEND_URL)
            .build()

    @Provides
    @Singleton
    @ServiceRetrofit
    fun provideServiceRetrofitBuilder(
        okHttpClient: OkHttpClient,
        builder: Retrofit.Builder,
    ): Retrofit =
        builder
            .baseUrl(BuildConfig.BACKEND_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    @ChatRetrofit
    fun provideChatRetrofitBuilder(
        okHttpClient: OkHttpClient,
        builder: Retrofit.Builder,
    ): Retrofit =
        builder
            .baseUrl(BuildConfig.WEBSOCKET_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    @ImageRetrofit
    fun provideImageRetrofitBuilder(callAdapterFactory: ResultCallAdapterFactory): Retrofit =
        Retrofit
            .Builder()
            .addConverterFactory(retroJson.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(callAdapterFactory)
            .baseUrl(BuildConfig.BACKEND_URL)
            .build()

    @Provides
    @Singleton
    @KakaoRestRetrofit
    fun provideKakaoRetrofitBuilder(
        interceptor: HttpLoggingInterceptor,
        builder: Retrofit.Builder,
    ): Retrofit =
        builder
            .baseUrl(BuildConfig.KAKAO_REST_URL)
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
    fun provideErrorInterceptor(eventManager: NetworkEventManager) = ErrorInterceptor(eventManager)

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenRepository: JwtRepository,
        authRepository: AuthenticationRepository,
        networkEventManager: NetworkEventManager,
    ) = AuthInterceptor(tokenRepository, authRepository, networkEventManager)

    @Provides
    @Singleton
    fun provideOkHttpBuilder(
        headerInterceptor: HeaderInterceptor,
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        errorInterceptor: ErrorInterceptor,
    ): OkHttpClient.Builder =
        OkHttpClient
            .Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(errorInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authInterceptor)

    @Provides
    @Singleton
    fun provideOkHttpClient(builder: OkHttpClient.Builder): OkHttpClient = builder.build()

    @Provides
    @Singleton
    fun provideLoggingInterceptor() =
        HttpLoggingInterceptor()
            .setLevel(
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.BODY
                },
            )
}
