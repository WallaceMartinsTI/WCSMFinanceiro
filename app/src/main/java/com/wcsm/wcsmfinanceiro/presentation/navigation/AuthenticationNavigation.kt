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
        startDestination = Screen.WelcomeScreen
    ) {
        composable<Screen.WelcomeScreen> {
            WelcomeView(
                onContinue = { authenticationNavController.navigate(Screen.LoginScreen) }
            )
        }

        composable<Screen.LoginScreen>{
            LoginView(
                onCreateAccount = { authenticationNavController.navigate(Screen.RegisterScreen) },
                onLogin = { authenticationNavController.navigate(Screen.MainNavigation) }
            )
        }

        composable<Screen.RegisterScreen> {
            RegisterView(
                onAlreadyHasAccount = { authenticationNavController.navigate(Screen.LoginScreen) },
                onRegister = { authenticationNavController.navigate(Screen.LoginScreen) }
            )
        }

        composable<Screen.MainNavigation> {
            MainNavigation()
        }
    }
}