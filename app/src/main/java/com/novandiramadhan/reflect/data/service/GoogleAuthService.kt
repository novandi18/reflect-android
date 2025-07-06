package com.novandiramadhan.reflect.data.service

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.novandiramadhan.reflect.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GoogleAuthService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val credentialManager: CredentialManager
) {
    suspend fun signIn(): Result<String> {
        return try {
            val request = createSignInRequest()
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            val googleIdTokenCredential = handleSignInResult(result)
            Result.success(googleIdTokenCredential)
        } catch (e: GetCredentialException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun createSignInRequest(): GetCredentialRequest {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    private fun handleSignInResult(result: GetCredentialResponse): String {
        val credential = result.credential

        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                return googleIdTokenCredential.idToken
            } catch (e: GoogleIdTokenParsingException) {
                throw Exception("Failed to parse Google ID token: ${e.message}")
            }
        } else {
            throw Exception("Unexpected credential type")
        }
    }
}