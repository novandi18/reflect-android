package com.novandiramadhan.reflect.di

import com.novandiramadhan.reflect.data.remote.api.ZenQuotesApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://zenquotes.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideZenQuotesApiService(retrofit: Retrofit): ZenQuotesApiService {
        return retrofit.create(ZenQuotesApiService::class.java)
    }
}