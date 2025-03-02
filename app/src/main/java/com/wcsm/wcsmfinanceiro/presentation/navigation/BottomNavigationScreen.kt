package com.wcsm.wcsmfinanceiro.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.ui.graphics.vector.ImageVector
import com.wcsm.wcsmfinanceiro.presentation.model.Screen

enum class BottomNavigationScreen(
    val title: String,
    val icon: ImageVector,
    val iconContentDescription: String,
    val screen: Screen
) {
    HOME(
        title = "Home",
        icon = Icons.Default.Home,
        iconContentDescription = "Ícone de casa",
        screen = Screen.HomeScreen
    ),
    BILLS(
        title = "Contas",
        icon = Icons.Default.Payments,
        iconContentDescription = "Ícone de pagamento",
        screen = Screen.BillsScreen
    ),
    WALLET(
        title = "Carteira",
        icon = Icons.Default.Wallet,
        iconContentDescription = "Ícone de carteira",
        screen = Screen.WalletScreen
    ),
    PLUS(
        title = "Plus",
        icon = Icons.Default.PostAdd,
        iconContentDescription = "Ícone de plus",
        screen = Screen.PlusScreen
    ),
    SETTINGS(
        title = "Configurações",
        icon = Icons.Default.Settings,
        iconContentDescription = "Ícone de configurações",
        screen = Screen.SettingsScreen
    )
}