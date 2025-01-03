package com.wcsm.wcsmfinanceiro.presentation.model

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    data object WelcomeScreen : Screen("welcome")
    @Serializable
    data object LoginScreen : Screen("login")
}
