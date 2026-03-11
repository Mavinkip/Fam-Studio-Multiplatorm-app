package com.famstudio.app.presentation.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Base ViewModel for MVI pattern.
 *
 * S = State  — immutable data class representing the full UI state
 * I = Intent — sealed class of all possible user actions
 *
 * Usage:
 *   class HomeViewModel : MviViewModel<HomeState, HomeIntent>(HomeState()) {
 *       override fun handleIntent(intent: HomeIntent) { ... }
 *   }
 */
abstract class MviViewModel<S, I>(initialState: S) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    protected fun updateState(update: S.() -> S) {
        _uiState.value = _uiState.value.update()
    }

    abstract fun handleIntent(intent: I)
}

/** Wraps async results cleanly */
sealed class Result<out T> {
    data object Loading : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val cause: Throwable? = null) : Result<Nothing>()
}

fun <T> Result<T>.onSuccess(block: (T) -> Unit): Result<T> {
    if (this is Result.Success) block(data)
    return this
}

fun <T> Result<T>.onError(block: (String) -> Unit): Result<T> {
    if (this is Result.Error) block(message)
    return this
}
