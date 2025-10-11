package com.novandiramadhan.reflect.domain.model

data class Setting(
    val reminderTime: String = "",
    val theme: String = "light",
    val language: String = "en",
    val useBiometric: Boolean = false
)