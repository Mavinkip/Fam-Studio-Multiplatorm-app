package com.famstudio.app.data.auth

import android.app.Activity
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

private const val WEB_CLIENT_ID =
    "903052396680-jqknmur6o5qn9nl2fin7fb99ujinmlg7.apps.googleusercontent.com"
private const val TAG = "GoogleAuth"

class GoogleAuthHelperAndroid : GoogleAuthHelper {

    override suspend fun getGoogleIdToken(activityContext: Any): GoogleTokenResult {
        val activity = activityContext as? Activity
        if (activity == null) {
            val msg = "Context is not an Activity: ${activityContext::class.simpleName}"
            Log.e(TAG, "❌ $msg")
            return GoogleTokenResult(error = msg)
        }

        Log.d(TAG, "✅ Activity OK. Launching Credential Manager...")
        val credentialManager = CredentialManager.create(activity)

        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(WEB_CLIENT_ID)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(request = request, context = activity)
            val token  = GoogleIdTokenCredential.createFrom(result.credential.data).idToken
            Log.d(TAG, "✅ Token received OK")
            GoogleTokenResult(token = token)

        } catch (e: GetCredentialCancellationException) {
            Log.w(TAG, "⚠️ Cancelled by user")
            GoogleTokenResult(error = "cancelled")
        } catch (e: NoCredentialException) {
            val msg = "No Google account found on device, or SHA-1 fingerprint mismatch in Firebase"
            Log.e(TAG, "❌ $msg", e)
            GoogleTokenResult(error = msg)
        } catch (e: GetCredentialException) {
            val msg = "${e::class.simpleName}: ${e.message}"
            Log.e(TAG, "❌ $msg", e)
            GoogleTokenResult(error = msg)
        } catch (e: Exception) {
            val msg = "${e::class.simpleName}: ${e.message}"
            Log.e(TAG, "❌ $msg", e)
            GoogleTokenResult(error = msg)
        }
    }
}