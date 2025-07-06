package com.novandiramadhan.reflect.domain.model

import com.google.firebase.Timestamp

data class Journal(
    val mood: String = "",
    val moodLevel: Int = 1,
    val triggers: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val note: String = "",
    val createdAt: Timestamp = Timestamp.now()
)