package com.novandiramadhan.reflect.util.biometric

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

object BiometricUtils {
    fun initBiometricPrompt(
        activity: FragmentActivity,
        listener: BiometricAuthListener
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                listener.onBiometricAuthenticateError(errorCode, errString.toString())
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                listener.onAuthenticationFailed()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                listener.onBiometricAuthenticateSuccess(result)
            }
        }
        return BiometricPrompt(activity, executor, callback)
    }

    fun createPromptInfo(
        title: String,
        description: String,
        negativeText: String,
    ): BiometricPrompt.PromptInfo =
        setBiometricPromptInfo(
            title,
            description,
            negativeText
        )

    private fun setBiometricPromptInfo(
        title: String,
        description: String,
        negativeText: String
    ): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setDescription(description)
            .setNegativeButtonText(negativeText)
            .setConfirmationRequired(false)
            .build()
    }

    private fun hasBiometricCapability(context: Context): Int {
        return BiometricManager.from(context)
            .canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL or BIOMETRIC_WEAK)
    }

    fun isBiometricReady(context: Context): Boolean =
        hasBiometricCapability(context) == BiometricManager.BIOMETRIC_SUCCESS
}
