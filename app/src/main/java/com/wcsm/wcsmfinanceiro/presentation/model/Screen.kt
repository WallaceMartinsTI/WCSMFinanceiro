package com.wcsm.wcsmfinanceiro.presentation.model

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    data object WelcomeView : Screen("welcome")
    @Serializable
    data object LoginView : Screen("login")
    @Serializable
    data object RegisterView : Screen("register")
}
