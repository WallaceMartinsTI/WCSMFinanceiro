package com.wcsm.wcsmfinanceiro.presentation.model

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    data object WelcomeScreen : Screen("welcome")
    @Serializable
    data object LoginScreen : Screen("login")
    @Serializable
    data object RegisterScreen : Screen("register")

    @Serializable
    data object MainNavigation: Screen("main_navigation")

    @Serializable
    data object HomeScreen : Screen("home")
    @Serializable
    data object BillsScreen : Screen("bills")
    @Serializable
    data object WalletScreen : Screen("wallet")
    @Serializable
    data object PlusScreen : Screen("plus")
    @Serializable
    data object SettingsScreen : Screen("settings")
}
