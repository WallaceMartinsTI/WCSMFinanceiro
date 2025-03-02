package com.wcsm.wcsmfinanceiro.presentation.model

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object WelcomeScreen : Screen
    @Serializable
    data object LoginScreen : Screen
    @Serializable
    data object RegisterScreen : Screen

    @Serializable
    data object MainNavigation : Screen

    @Serializable
    data object HomeScreen : Screen
    @Serializable
    data object BillsScreen : Screen
    @Serializable
    data object WalletScreen : Screen
    @Serializable
    data object PlusScreen : Screen
    @Serializable
    data object SettingsScreen : Screen
}
