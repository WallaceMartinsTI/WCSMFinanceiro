package com.wcsm.wcsmfinanceiro.presentation.ui.view.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.wcsm.wcsmfinanceiro.presentation.model.RegisterState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class RegisterViewModel : ViewModel() {

    private val _registerStateFlow = MutableStateFlow(RegisterState())
    val registerStateFlow = _registerStateFlow.asStateFlow()

    fun updateRegisterState(updatedState: RegisterState) {
        _registerStateFlow.value = updatedState
    }

    fun isRegisterStateValid(): Boolean {
        val isNameValid = validateName(registerStateFlow.value.name)
        val isEmailValid = validateEmail(registerStateFlow.value.email)
        val isPasswordValid = validatePassword(registerStateFlow.value.password)

        updateRegisterState(
            registerStateFlow.value.copy(
                nameErrorMessage = isNameValid.second,
                emailErrorMessage = isEmailValid.second,
                passwordErrorMessage = isPasswordValid.second
            )
        )

        return isNameValid.first && isEmailValid.first && isPasswordValid.first
    }

    private fun validateName(name: String): Pair<Boolean, String> {
        return if(name.isBlank()) {
            Pair(false, "Você deve informar o nome.")
        } else if(name.length < 3) {
            Pair(false, "Nome muito curto (mín. 3 caracteres).")
        } else if(name.length > 20) {
            Pair(false, "Nome muito grande (max. 20 caracteres).")
        } else {
            Pair(true, "")
        }
    }

    private fun validateEmail(email: String): Pair<Boolean, String> {
        return if(email.isBlank()) {
            Pair(false, "Você deve informar um e-mail.")
        } else if(email.length < 5) {
            Pair(false, "E-mail muito curto (mín. 10 caracteres).")
        } else if(email.length > 50) {
            Pair(false, "E-mail muito grande (max. 200 caracteres).")
        } else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Pair(false, "E-mail inválido, tente outro.")
        } else {
            Pair(true, "")
        }
    }

    private fun validatePassword(password: String): Pair<Boolean, String> {
        return if(password.isBlank()) {
            Pair(false, "Você deve informar uma senha.")
        } else if(password.length < 5) {
            Pair(false, "Senha muito curta (mín. 5 caracteres).")
        } else if(password.length > 50) {
            Pair(false, "Senha muito grande (max. 50 caracteres).")
        } else {
            Pair(true, "")
        }
    }
}