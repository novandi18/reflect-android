package com.novandiramadhan.reflect.di

import com.novandiramadhan.reflect.data.repository.MoodHistoryRepositoryImpl
import com.novandiramadhan.reflect.data.repository.MoodRepositoryImpl
import com.novandiramadhan.reflect.data.repository.NotificationRepositoryImpl
import com.novandiramadhan.reflect.data.repository.SummaryRepositoryImpl
import com.novandiramadhan.reflect.data.repository.UserRepositoryImpl
import com.novandiramadhan.reflect.domain.repository.MoodHistoryRepository
import com.novandiramadhan.reflect.domain.repository.MoodRepository
import com.novandiramadhan.reflect.domain.repository.NotificationRepository
import com.novandiramadhan.reflect.domain.repository.SummaryRepository
import com.novandiramadhan.reflect.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun bindMoodHistoryRepository(
        impl: MoodHistoryRepositoryImpl
    ): MoodHistoryRepository

    @Binds
    abstract fun bindMoodRepository(
        impl: MoodRepositoryImpl
    ): MoodRepository

    @Binds
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository

    @Binds
    abstract fun bindSummaryRepository(
        impl: SummaryRepositoryImpl
    ): SummaryRepository
}