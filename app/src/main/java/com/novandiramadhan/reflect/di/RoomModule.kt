package com.novandiramadhan.reflect.di

import android.content.Context
import androidx.room.Room
import com.novandiramadhan.reflect.data.local.room.ReflectDatabase
import com.novandiramadhan.reflect.util.RoomConstrains
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ReflectDatabase {
        return Room.databaseBuilder(
            context,
            ReflectDatabase::class.java,
            RoomConstrains.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideMoodHistoryDao(database: ReflectDatabase) = database.moodHistoryDao()

    @Provides
    @Singleton
    fun provideNotificationDao(database: ReflectDatabase) = database.notificationDao()

    @Provides
    @Singleton
    fun provideMonthlySummaryDao(database: ReflectDatabase) = database.monthlySummaryDao()

    @Provides
    @Singleton
    fun provideWeeklySummaryDao(database: ReflectDatabase) = database.weeklySummaryDao()
}