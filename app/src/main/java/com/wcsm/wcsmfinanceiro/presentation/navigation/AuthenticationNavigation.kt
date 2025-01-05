package com.wcsm.wcsmfinanceiro.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wcsm.wcsmfinanceiro.presentation.model.Screen
import com.wcsm.wcsmfinanceiro.presentation.ui.view.login.LoginView
import com.wcsm.wcsmfinanceiro.presentation.ui.view.WelcomeView
import com.wcsm.wcsmfinanceiro.presentation.ui.view.register.RegisterView

@Composable
fun AuthenticationNavigation() {
    val authenticationNavController = rememberNavController()

    NavHost(
        navController = authenticationNavController,
        startDestination = Screen.WelcomeScreen.route
    ) {
        composable(route = Screen.WelcomeScreen.route) {
            WelcomeView(
                onContinue = { authenticationNavController.navigate(Screen.LoginScreen.route) }
            )
        }

        composable(route = Screen.LoginScreen.route) {
            LoginView(
                onCreateAccount = { authenticationNavController.navigate(Screen.RegisterScreen.route) },
                onLogin = { authenticationNavController.navigate(Screen.MainNavigation.route) }
            )
        }

        composable(route = Screen.RegisterScreen.route) {
            RegisterView(
                onAlreadyHasAccount = { authenticationNavController.navigate(Screen.LoginScreen.route) },
                onRegister = { authenticationNavController.navigate(Screen.LoginScreen.route) }
            )
        }

        composable(route = Screen.MainNavigation.route) {
            MainNavigation()
        }
    }
}