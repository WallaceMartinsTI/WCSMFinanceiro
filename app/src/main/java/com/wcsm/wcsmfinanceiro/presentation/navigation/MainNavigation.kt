package com.wcsm.wcsmfinanceiro.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wcsm.wcsmfinanceiro.presentation.model.Screen
import com.wcsm.wcsmfinanceiro.presentation.ui.component.BottomNavigationBarItem
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.view.bills.BillsView
import com.wcsm.wcsmfinanceiro.presentation.ui.view.home.HomeView
import com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.PlusView
import com.wcsm.wcsmfinanceiro.presentation.ui.view.settings.SettingsView
import com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet.WalletView
import com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet.WalletViewModel

@Composable
fun MainNavigation() {
    val mainNavController = rememberNavController()

    val currentDestination = mainNavController.currentBackStackEntryAsState().value?.destination

    val walletViewModel: WalletViewModel = hiltViewModel()

    Scaffold(
        bottomBar = {
            NavigationBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    BottomNavigationScreen.entries.forEach { destination ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.hasRoute(destination.screen::class)
                        } == true

                        BottomNavigationBarItem(
                            label = destination.title,
                            icon = destination.icon,
                            iconDescription = destination.iconContentDescription,
                            selected = selected
                        ) {
                            mainNavController.navigate(destination.screen) {
                                popUpTo(mainNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = mainNavController,
            startDestination = Screen.HomeScreen,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<Screen.HomeScreen> {
                HomeView()
            }

            composable<Screen.BillsScreen> {
                BillsView(walletViewModel = walletViewModel)
            }

            composable<Screen.WalletScreen> {
                WalletView(walletViewModel = walletViewModel)
            }

            composable<Screen.PlusScreen> {
                PlusView()
            }

            composable<Screen.SettingsScreen> {
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