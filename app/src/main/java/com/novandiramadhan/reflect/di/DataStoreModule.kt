package com.novandiramadhan.reflect.di

import android.content.Context
import com.novandiramadhan.reflect.data.local.datastore.MoodDataStoreImpl
import com.novandiramadhan.reflect.data.local.datastore.SettingDataStoreImpl
import com.novandiramadhan.reflect.data.local.datastore.UserDataStoreImpl
import com.novandiramadhan.reflect.data.local.datastore.WelcomeDataStoreImpl
import com.novandiramadhan.reflect.domain.datastore.MoodDataStore
import com.novandiramadhan.reflect.domain.datastore.SettingDataStore
import com.novandiramadhan.reflect.domain.datastore.UserDataStore
import com.novandiramadhan.reflect.domain.datastore.WelcomeDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideWelcomeDataStore(@ApplicationContext context: Context): WelcomeDataStore =
        WelcomeDataStoreImpl(context)

    @Provides
    @Singleton
    fun provideUserDataStore(@ApplicationContext context: Context): UserDataStore =
        UserDataStoreImpl(context)

    @Provides
    @Singleton
    fun provideSettingDataStore(@ApplicationContext context: Context): SettingDataStore =
        SettingDataStoreImpl(context)

    @Provides
    @Singleton
    fun provideMoodDataStore(@ApplicationContext context: Context): MoodDataStore =
        MoodDataStoreImpl(context)
}