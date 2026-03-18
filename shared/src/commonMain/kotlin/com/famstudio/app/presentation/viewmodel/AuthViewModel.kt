package com.famstudio.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.famstudio.app.data.auth.AuthRepository
import com.famstudio.app.data.auth.AuthResult
import com.famstudio.app.data.auth.GoogleAuthHelper
import com.famstudio.app.domain.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email:           String  = "",
    val password:        String  = "",
    val isLoading:       Boolean = false,
    val isGoogleLoading: Boolean = false,
    val error:           String? = null,
    val isSuccess:       Boolean = false
)

class LoginViewModel(
    private val repo:         AuthRepository,
    private val googleHelper: GoogleAuthHelper
) : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun onEmailChange(v: String)    { _state.value = _state.value.copy(email = v, error = null) }
    fun onPasswordChange(v: String) { _state.value = _state.value.copy(password = v, error = null) }

    fun signIn() {
        val s = _state.value
        if (s.email.isBlank() || s.password.isBlank()) {
            _state.value = s.copy(error = "Email and password are required"); return
        }
        _state.value = s.copy(isLoading = true, error = null)
        viewModelScope.launch {
            when (val r = repo.signIn(s.email.trim(), s.password)) {
                is AuthResult.Success -> _state.value = _state.value.copy(isLoading = false, isSuccess = true)
                is AuthResult.Error   -> _state.value = _state.value.copy(isLoading = false, error = r.message)
            }
        }
    }

    fun signInWithGoogle(activityContext: Any) {
        _state.value = _state.value.copy(isGoogleLoading = true, error = null)
        viewModelScope.launch {
            val result = googleHelper.getGoogleIdToken(activityContext)
            when {
                result.token != null -> {
                    when (val r = repo.signInWithGoogle(result.token)) {
                        is AuthResult.Success -> _state.value = _state.value.copy(isGoogleLoading = false, isSuccess = true)
                        is AuthResult.Error   -> _state.value = _state.value.copy(isGoogleLoading = false, error = r.message)
                    }
                }
                result.error == "cancelled" -> {
                    _state.value = _state.value.copy(isGoogleLoading = false, error = null)
                }
                else -> {
                    _state.value = _state.value.copy(isGoogleLoading = false, error = result.error)
                }
            }
        }
    }
}

data class RegisterUiState(
    val name:            String   = "",
    val email:           String   = "",
    val password:        String   = "",
    val confirmPass:     String   = "",
    val role:            UserRole = UserRole.CLIENT,
    val isLoading:       Boolean  = false,
    val isGoogleLoading: Boolean  = false,
    val error:           String?  = null,
    val isSuccess:       Boolean  = false
)

class RegisterViewModel(
    private val repo:         AuthRepository,
    private val googleHelper: GoogleAuthHelper
) : ViewModel() {
    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    fun onNameChange(v: String)        { _state.value = _state.value.copy(name = v, error = null) }
    fun onEmailChange(v: String)       { _state.value = _state.value.copy(email = v, error = null) }
    fun onPasswordChange(v: String)    { _state.value = _state.value.copy(password = v, error = null) }
    fun onConfirmPassChange(v: String) { _state.value = _state.value.copy(confirmPass = v, error = null) }
    fun onRoleChange(r: UserRole)      { _state.value = _state.value.copy(role = r) }

    fun register() {
        val s = _state.value
        when {
            s.name.isBlank()            -> { _state.value = s.copy(error = "Name is required"); return }
            s.email.isBlank()           -> { _state.value = s.copy(error = "Email is required"); return }
            s.password.length < 6       -> { _state.value = s.copy(error = "Password must be at least 6 characters"); return }
            s.password != s.confirmPass -> { _state.value = s.copy(error = "Passwords do not match"); return }
        }
        _state.value = s.copy(isLoading = true, error = null)
        viewModelScope.launch {
            when (val r = repo.register(s.email.trim(), s.password, s.name.trim(), s.role)) {
                is AuthResult.Success -> _state.value = _state.value.copy(isLoading = false, isSuccess = true)
                is AuthResult.Error   -> _state.value = _state.value.copy(isLoading = false, error = r.message)
            }
        }
    }

    fun signUpWithGoogle(activityContext: Any) {
        _state.value = _state.value.copy(isGoogleLoading = true, error = null)
        viewModelScope.launch {
            val result = googleHelper.getGoogleIdToken(activityContext)
            when {
                result.token != null -> {
                    when (val r = repo.signInWithGoogle(result.token)) {
                        is AuthResult.Success -> _state.value = _state.value.copy(isGoogleLoading = false, isSuccess = true)
                        is AuthResult.Error   -> _state.value = _state.value.copy(isGoogleLoading = false, error = r.message)
                    }
                }
                result.error == "cancelled" -> {
                    _state.value = _state.value.copy(isGoogleLoading = false, error = null)
                }
                else -> {
                    _state.value = _state.value.copy(isGoogleLoading = false, error = result.error)
                }
            }
        }
    }
}

data class ForgotPassUiState(
    val email:     String  = "",
    val isLoading: Boolean = false,
    val error:     String? = null,
    val isSuccess: Boolean = false
)

class ForgotPassViewModel(private val repo: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow(ForgotPassUiState())
    val state: StateFlow<ForgotPassUiState> = _state.asStateFlow()

    fun onEmailChange(v: String) { _state.value = _state.value.copy(email = v, error = null) }

    fun sendReset() {
        val s = _state.value
        if (s.email.isBlank()) { _state.value = s.copy(error = "Email is required"); return }
        _state.value = s.copy(isLoading = true, error = null)
        viewModelScope.launch {
            when (val r = repo.sendPasswordReset(s.email.trim())) {
                is AuthResult.Success -> _state.value = _state.value.copy(isLoading = false, isSuccess = true)
                is AuthResult.Error   -> _state.value = _state.value.copy(isLoading = false, error = r.message)
            }
        }
    }
}