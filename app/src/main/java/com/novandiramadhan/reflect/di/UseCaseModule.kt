package com.novandiramadhan.reflect.di

import com.novandiramadhan.reflect.domain.interactor.MoodHistoryInteractor
import com.novandiramadhan.reflect.domain.interactor.MoodInteractor
import com.novandiramadhan.reflect.domain.interactor.NotificationInteractor
import com.novandiramadhan.reflect.domain.interactor.SummaryInteractor
import com.novandiramadhan.reflect.domain.interactor.UserInteractor
import com.novandiramadhan.reflect.domain.usecase.MoodHistoryUseCase
import com.novandiramadhan.reflect.domain.usecase.MoodUseCase
import com.novandiramadhan.reflect.domain.usecase.NotificationUseCase
import com.novandiramadhan.reflect.domain.usecase.SummaryUseCase
import com.novandiramadhan.reflect.domain.usecase.UserUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    @Singleton
    abstract fun bindUserUseCase(interactor: UserInteractor): UserUseCase

    @Binds
    @Singleton
    abstract fun bindMoodHistoryUseCase(interactor: MoodHistoryInteractor): MoodHistoryUseCase

    @Binds
    @Singleton
    abstract fun bindMoodUseCase(interactor: MoodInteractor): MoodUseCase

    @Binds
    @Singleton
    abstract fun bindNotificationUseCase(interactor: NotificationInteractor): NotificationUseCase

    @Binds
    @Singleton
    abstract fun bindSummaryUseCase(interactor: SummaryInteractor): SummaryUseCase
}