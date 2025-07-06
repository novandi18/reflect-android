package com.novandiramadhan.reflect.domain.interactor

import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.domain.model.User
import com.novandiramadhan.reflect.domain.repository.UserRepository
import com.novandiramadhan.reflect.domain.usecase.UserUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserInteractor @Inject constructor(
    private val userRepository: UserRepository,
): UserUseCase {
    override fun login(
        email: String,
        password: String
    ): Flow<Resource<User>> = userRepository.login(email, password)

    override fun register(
        user: User,
        password: String
    ): Flow<Resource<User>> = userRepository.register(user, password)

    override fun getCurrentUser(): Flow<Resource<User?>> =
        userRepository.getCurrentUser()

    override fun signInWithGoogle(idToken: String): Flow<Resource<User>> =
        userRepository.signInWithGoogle(idToken)

    override fun logout(): Flow<Resource<Unit>> = userRepository.logout()
}