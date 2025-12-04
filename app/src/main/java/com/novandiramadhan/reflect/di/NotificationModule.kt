package com.novandiramadhan.reflect.di

import android.content.Context
import com.novandiramadhan.reflect.domain.usecase.NotificationUseCase
import com.novandiramadhan.reflect.notification.scheduler.NotificationScheduler
import com.novandiramadhan.reflect.notification.manager.ReminderNotificationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Singleton
    @Provides
    fun provideReminderNotificationManager(
        @ApplicationContext context: Context,
        notificationUseCase: NotificationUseCase
    ) = ReminderNotificationManager(context, notificationUseCase)


    @Singleton
    @Provides
    fun provideNotificationScheduler(
        @ApplicationContext context: Context
    ) = NotificationScheduler(context)
}