package com.wcsm.wcsmfinanceiro.presentation.model

data class RegisterState(
    var name: String = "",
    var nameErrorMessage: String = "",
    var email: String = "",
    var emailErrorMessage: String = "",
    var password: String = "",
    var passwordErrorMessage: String = ""
)