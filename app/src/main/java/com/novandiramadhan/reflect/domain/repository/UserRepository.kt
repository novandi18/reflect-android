package com.novandiramadhan.reflect.domain.repository

import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun login(email: String, password: String): Flow<Resource<User>>
    fun register(user: User, password: String): Flow<Resource<User>>
    fun getCurrentUser(): Flow<Resource<User?>>
    fun signInWithGoogle(idToken: String): Flow<Resource<User>>
    fun logout(): Flow<Resource<Unit>>
}