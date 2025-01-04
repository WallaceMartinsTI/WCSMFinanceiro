package com.wcsm.wcsmfinanceiro.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wcsm.wcsmfinanceiro.presentation.model.Screen
import com.wcsm.wcsmfinanceiro.presentation.ui.view.login.LoginView
import com.wcsm.wcsmfinanceiro.presentation.ui.view.WelcomeView
import com.wcsm.wcsmfinanceiro.presentation.ui.view.register.RegisterView

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.WelcomeView.route
    ) {
        composable(route = Screen.WelcomeView.route) {
            WelcomeView {
                navController.navigate(Screen.LoginView.route)
            }
        }

        composable(route = Screen.LoginView.route) {
            LoginView(
                onCreateAccount = { navController.navigate(Screen.RegisterView.route) },
                onLogin = { navController.navigate(Screen.WelcomeView.route) }
            )
        }

        composable(route = Screen.RegisterView.route) {
            RegisterView(
                onAlreadyHasAccount = { navController.navigate(Screen.LoginView.route) },
                onRegister = {}
            )
        }
    }
}