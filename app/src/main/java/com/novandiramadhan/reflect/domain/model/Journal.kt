package com.novandiramadhan.reflect.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Journal(
    @PropertyName("mood")
    val mood: String = "",

//    @PropertyName("mood_level")
    val moodLevel: Int = 1,

    @PropertyName("triggers")
    val triggers: List<String> = emptyList(),

    @PropertyName("tags")
    val tags: List<String> = emptyList(),

    @PropertyName("note")
    val note: String = "",

    @PropertyName("createdAt")
    val createdAt: Timestamp = Timestamp.now()
)