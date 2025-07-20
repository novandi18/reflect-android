package com.novandiramadhan.reflect.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Journal(
    val id: String = "",
    val mood: String = "",
    @PropertyName("mood_level") val moodLevel: Int = 1,
    val triggers: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val note: String = "",
    val createdAt: Timestamp = Timestamp.now()
)