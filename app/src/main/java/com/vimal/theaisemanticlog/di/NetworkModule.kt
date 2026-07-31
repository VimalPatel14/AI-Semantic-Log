package com.vimal.theaisemanticlog.di

import com.vimal.theaisemanticlog.data.remote.ApiConstants
import com.vimal.theaisemanticlog.data.remote.api.LogApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideProductService(
        retrofit: Retrofit
    ): LogApiService {
        return retrofit.create(LogApiService::class.java)
    }
}