package com.example.gallery_sync_app.screens.di

import com.example.gallery_sync_app.screens.apis.ImageBBApi
import com.example.gallery_sync_app.screens.apis.KtorSeverApi
import com.example.gallery_sync_app.screens.constants.DefaultValues
import com.example.gallery_sync_app.screens.di.customAnnotations.ImgBBRetrofit
import com.example.gallery_sync_app.screens.di.customAnnotations.KtorRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApisImp {
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
        return okHttpClient

    }

    @Singleton
    @Provides
    @ImgBBRetrofit
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(DefaultValues.baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Singleton
    @Provides
    @KtorRetrofit
    fun provideKtorRetrofit(client: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .baseUrl(DefaultValues.baseUrl2)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    @Singleton
    @Provides
    fun provideApi( @ImgBBRetrofit retrofit: Retrofit): ImageBBApi {
        return retrofit.create<ImageBBApi>(ImageBBApi::class.java)
    }
    @Singleton
    @Provides
    fun provideKtorApi( @KtorRetrofit retrofit: Retrofit): KtorSeverApi{
return retrofit.create<KtorSeverApi>(KtorSeverApi::class.java)
    }

}