package com.novandiramadhan.reflect.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.novandiramadhan.reflect.data.service.GoogleAuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager {
        return CredentialManager.create(context)
    }

    @Provides
    @Singleton
    fun provideGoogleAuthClient(
        @ApplicationContext context: Context,
        credentialManager: CredentialManager
    ): GoogleAuthService {
        return GoogleAuthService(context, credentialManager)
    }
}