package com.novandiramadhan.reflect.util

import androidx.core.util.PatternsCompat

object FieldValidation {
    fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }
}