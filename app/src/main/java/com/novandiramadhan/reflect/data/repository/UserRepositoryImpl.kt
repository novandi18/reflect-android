package com.novandiramadhan.reflect.data.repository

import android.content.Context
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.data.resource.Resource
import com.novandiramadhan.reflect.domain.model.User
import com.novandiramadhan.reflect.domain.repository.UserRepository
import com.novandiramadhan.reflect.util.FirestoreCollections
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
): UserRepository {
    override fun login(email: String, password: String): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())

            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid

            if (userId == null) {
                emit(Resource.Error(message = context.getString(R.string.login_failed)))
                return@flow
            }

            val userDoc = firestore.collection(FirestoreCollections.USERS).document(userId)
                .get().await()

            if (!userDoc.exists()) {
                firebaseAuth.signOut()
                emit(Resource.Error(message = context.getString(R.string.user_not_found)))
                return@flow
            }

            val userData = userDoc.toObject(User::class.java)?.copy(id = userId)

            if (userData != null) {
                emit(Resource.Success(userData))
            } else {
                emit(Resource.Error(message = context.getString(R.string.login_failed)))
            }

        } catch (_: FirebaseAuthInvalidUserException) {
            emit(Resource.Error(message = context.getString(R.string.user_not_found)))
        } catch (_: FirebaseAuthInvalidCredentialsException) {
            emit(Resource.Error(message = context.getString(R.string.user_not_found)))
        } catch (_: FirebaseNetworkException) {
            emit(Resource.Error(message = context.getString(R.string.network_error)))
        } catch (_: Exception) {
            emit(Resource.Error(message = context.getString(R.string.generic_error)))
        }
    }

    override fun register(user: User, password: String): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())

            val authResult = firebaseAuth.createUserWithEmailAndPassword(user.email, password).await()
            val userId = authResult.user?.uid

            if (userId == null) {
                emit(Resource.Error(message = context.getString(R.string.register_failed)))
                return@flow
            }

            val newUser = user.copy(
                id = userId,
                createdAt = Timestamp.now()
            )

            firestore.collection(FirestoreCollections.USERS)
                .document(userId)
                .set(newUser).await()

            emit(Resource.Success(newUser))
        } catch (_: FirebaseAuthUserCollisionException) {
            emit(Resource.Error(message = context.getString(R.string.user_exists)))
        } catch (_: FirebaseAuthInvalidCredentialsException) {
            emit(Resource.Error(message = context.getString(R.string.register_failed)))
        } catch (_: FirebaseNetworkException) {
            emit(Resource.Error(message = context.getString(R.string.network_error)))
        } catch (_: Exception) {
            emit(Resource.Error(message = context.getString(R.string.generic_error)))
        }
    }

    override fun getCurrentUser(): Flow<Resource<User?>> = flow {
        emit(Resource.Loading())

        try {
            val currentUser = firebaseAuth.currentUser

            if (currentUser == null) {
                emit(Resource.Success(null))
                return@flow
            }

            val userDoc = firestore.collection(FirestoreCollections.USERS)
                .document(currentUser.uid).get().await()

            if (userDoc.exists()) {
                val userData = userDoc.toObject(User::class.java)?.copy(id = currentUser.uid)
                emit(Resource.Success(userData))
            } else {
                firebaseAuth.signOut()
                emit(Resource.Success(null))
            }
        } catch (_: Exception) {
            emit(Resource.Error(message = context.getString(R.string.generic_error)))
        }
    }

    override fun signInWithGoogle(idToken: String): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val userId = authResult.user?.uid

            if (userId == null) {
                emit(Resource.Error(message = context.getString(R.string.google_auth_failed)))
                return@flow
            }

            val googleUser = firebaseAuth.currentUser
            val user = User(
                id = userId,
                name = googleUser?.displayName ?: "",
                email = googleUser?.email ?: "",
                createdAt = Timestamp.now()
            )

            firestore.collection(FirestoreCollections.USERS)
                .document(userId)
                .set(user)
                .await()

            emit(Resource.Success(user))
        } catch (_: FirebaseAuthInvalidUserException) {
            emit(Resource.Error(message = context.getString(R.string.user_not_found)))
        } catch (_: FirebaseAuthInvalidCredentialsException) {
            emit(Resource.Error(message = context.getString(R.string.google_auth_failed)))
        } catch (_: FirebaseNetworkException) {
            emit(Resource.Error(message = context.getString(R.string.network_error)))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(message = context.getString(R.string.generic_error)))
        }
    }

    override fun logout(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            firebaseAuth.signOut()
            emit(Resource.Success(Unit))
        } catch (_: Exception) {
            emit(Resource.Error(message = context.getString(R.string.generic_error)))
        }
    }
}