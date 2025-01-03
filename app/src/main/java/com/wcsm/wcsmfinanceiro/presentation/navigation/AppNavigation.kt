package com.wcsm.wcsmfinanceiro.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wcsm.wcsmfinanceiro.presentation.model.Screen
import com.wcsm.wcsmfinanceiro.presentation.ui.view.login.LoginView
import com.wcsm.wcsmfinanceiro.presentation.ui.view.WelcomeView

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.WelcomeScreen.route
    ) {
        composable(route = Screen.WelcomeScreen.route) {
            WelcomeView {
                navController.navigate(Screen.LoginScreen.route)
            }
        }

        composable(route = Screen.LoginScreen.route) {
            LoginView {
                navController.navigate(Screen.WelcomeScreen.route)
            }
        }
    }
}