package com.famstudio.app.data.auth

import com.famstudio.app.domain.model.UserRole
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore

class AuthRepositoryImpl : AuthRepository {

    private val auth      = Firebase.auth
    private val firestore = Firebase.firestore

    override suspend fun signIn(email: String, password: String): AuthResult {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password)
            val uid    = result.user?.uid ?: return AuthResult.Error("Sign in failed")
            val user   = fetchUserFromFirestore(uid) ?: return AuthResult.Error("User profile not found")
            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Sign in failed")
        }
    }

    override suspend fun signInWithGoogle(idToken: String): AuthResult {
        return try {
            val credential = GoogleAuthProvider.credential(idToken, null)
            val result     = auth.signInWithCredential(credential)
            val firebaseUser = result.user ?: return AuthResult.Error("Google sign in failed")
            val uid        = firebaseUser.uid

            // Check if user profile already exists in Firestore
            val existingUser = fetchUserFromFirestore(uid)
            if (existingUser != null) {
                return AuthResult.Success(existingUser)
            }

            // First time Google sign-in — create profile with CLIENT role by default
            val name  = firebaseUser.displayName ?: ""
            val email = firebaseUser.email ?: ""
            firestore.collection("users").document(uid).set(
                mapOf(
                    "uid"        to uid,
                    "email"      to email,
                    "name"       to name,
                    "role"       to UserRole.CLIENT.name,
                    "avatarUrl"  to (firebaseUser.photoURL?.toString() ?: ""),
                    "isVerified" to false
                )
            )
            AuthResult.Success(
                FamUser(uid = uid, email = email, name = name, role = UserRole.CLIENT)
            )
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Google sign in failed")
        }
    }

    override suspend fun register(
        email: String, password: String, name: String, role: UserRole
    ): AuthResult {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password)
            val uid    = result.user?.uid ?: return AuthResult.Error("Registration failed")
            firestore.collection("users").document(uid).set(
                mapOf(
                    "uid"        to uid,
                    "email"      to email,
                    "name"       to name,
                    "role"       to role.name,
                    "avatarUrl"  to "",
                    "isVerified" to false
                )
            )
            AuthResult.Success(FamUser(uid = uid, email = email, name = name, role = role))
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Registration failed")
        }
    }

    override suspend fun sendPasswordReset(email: String): AuthResult {
        return try {
            auth.sendPasswordResetEmail(email)
            AuthResult.Success(FamUser("", email, "", UserRole.CLIENT))
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Password reset failed")
        }
    }

    override suspend fun signOut() { auth.signOut() }

    override suspend fun currentUser(): FamUser? {
        val uid = auth.currentUser?.uid ?: return null
        return fetchUserFromFirestore(uid)
    }

    override fun isLoggedIn(): Boolean = auth.currentUser != null

    private suspend fun fetchUserFromFirestore(uid: String): FamUser? {
        return try {
            val doc = firestore.collection("users").document(uid).get()
            if (!doc.exists) return null
            FamUser(
                uid       = uid,
                email     = doc.get("email")     ?: "",
                name      = doc.get("name")      ?: "",
                role      = UserRole.valueOf(doc.get<String>("role") ?: "CLIENT"),
                avatarUrl = doc.get("avatarUrl") ?: ""
            )
        } catch (e: Exception) { null }
    }
}