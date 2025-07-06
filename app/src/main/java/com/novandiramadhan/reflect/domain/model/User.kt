package com.novandiramadhan.reflect.domain.model

import com.google.firebase.Timestamp

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val createdAt: Timestamp? = null
)