package com.wcsm.wcsmfinanceiro.presentation.model

data class LoginState(
    var email: String = "",
    var emailErrorMessage: String = "",
    var password: String = "",
    var passwordErrorMessage: String = ""
)