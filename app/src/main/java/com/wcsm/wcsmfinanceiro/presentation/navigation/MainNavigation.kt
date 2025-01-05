package com.wcsm.wcsmfinanceiro.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wcsm.wcsmfinanceiro.presentation.model.Screen
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.view.bills.BillsView
import com.wcsm.wcsmfinanceiro.presentation.ui.view.home.HomeView
import com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.PlusView
import com.wcsm.wcsmfinanceiro.presentation.ui.view.settings.SettingsView
import com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet.WalletView

@Composable
fun MainNavigation() {
    val mainNavController = rememberNavController()

    val currentBackStackEntry by mainNavController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            BottomBar(
                actualScreen = currentBackStackEntry?.destination?.route ?: "Home",
                onNavigateToSelectedScreen = { route ->
                    mainNavController.navigate(route)
                },
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = mainNavController,
            startDestination = Screen.HomeScreen.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = Screen.HomeScreen.route) {
                HomeView()
            }

            composable(route = Screen.BillsScreen.route) {
                BillsView()
            }

            composable(route = Screen.WalletScreen.route) {
                WalletView()
            }

            composable(route = Screen.PlusScreen.route) {
                PlusView()
            }

            composable(route = Screen.SettingsScreen.route) {
                SettingsView()
            }
        }
    }
}

@Preview
@Composable
private fun MainNavigationPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        MainNavigation()
    }
}