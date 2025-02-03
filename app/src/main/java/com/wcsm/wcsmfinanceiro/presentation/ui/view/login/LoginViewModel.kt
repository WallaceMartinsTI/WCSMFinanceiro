package com.wcsm.wcsmfinanceiro.presentation.ui.view.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wcsm.wcsmfinanceiro.presentation.model.LoginState
import com.wcsm.wcsmfinanceiro.presentation.model.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _loginStateFlow = MutableStateFlow(LoginState())
    val loginStateFlow = _loginStateFlow.asStateFlow()

    private val _uiState = MutableStateFlow(UiState<Nothing>())
    val uiState = _uiState.asStateFlow()

    fun updateLoginState(updatedState: LoginState) {
        _loginStateFlow.value = updatedState
    }

    fun resetUiState() {
        _uiState.value = UiState()
    }

    fun loginUser(loginState: LoginState) { // TEMP FOR TEST
        resetErrorMessages()

        viewModelScope.launch(Dispatchers.IO) {
            // HANDLE UI STATE
            if(isLoginStateValid()) {
                _uiState.value = uiState.value.copy(
                    isLoading = false,
                    success = true
                )
            }
        }
    }

    private fun resetErrorMessages() {
        updateLoginState(
            loginStateFlow.value.copy(
                emailErrorMessage = "",
                passwordErrorMessage = ""
            )
        )
    }

    private fun isLoginStateValid(): Boolean {
        val isEmailValid = validateEmail(loginStateFlow.value.email)
        val isPasswordValid = validatePassword(loginStateFlow.value.password)

        updateLoginState(
            loginStateFlow.value.copy(
                emailErrorMessage = isEmailValid.second,
                passwordErrorMessage = isPasswordValid.second
            )
        )

        return isEmailValid.first && isPasswordValid.first
    }

    private fun validateEmail(email: String): Pair<Boolean, String> {
        return if(email.isBlank()) {
            Pair(false, "Você deve informar um e-mail.")
        } else {
            Pair(true, "")
        }
    }

    private fun validatePassword(password: String): Pair<Boolean, String> {
        return if(password.isBlank()) {
            Pair(false, "Você deve informar uma senha.")
        } else {
            Pair(true, "")
        }
    }
}