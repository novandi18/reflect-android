package com.novandiramadhan.reflect.data.local.locale

import com.novandiramadhan.reflect.domain.model.Language

object AppLocale {
    val languages = listOf(
        Language(
            code = "en",
            displayLanguage = "English"
        ),
        Language(
            code = "id",
            displayLanguage = "Bahasa Indonesia"
        )
    )
}