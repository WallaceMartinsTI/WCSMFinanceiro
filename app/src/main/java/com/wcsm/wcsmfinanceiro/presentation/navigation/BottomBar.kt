package com.wcsm.wcsmfinanceiro.presentation.navigation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.wcsm.wcsmfinanceiro.presentation.model.BottomBarItem
import com.wcsm.wcsmfinanceiro.presentation.model.Screen
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnBackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme

@Composable
fun BottomBar(
    actualScreen: String,
    onNavigateToSelectedScreen: (route: String) -> Unit
) {
    val bottomBarItems = listOf(
        BottomBarItem(
            title = "Home",
            icon = Icons.Default.Home,
            iconContentDescription = "Ícone de casa",
            route = Screen.HomeScreen.route
        ),
        BottomBarItem(
            title = "Contas",
            icon = Icons.Default.Payments,
            iconContentDescription = "Ícone de pagamento",
            route = Screen.BillsScreen.route
        ),
        BottomBarItem(
            title = "Carteira",
            icon = Icons.Default.Wallet,
            iconContentDescription = "Ícone de carteira",
            route = Screen.WalletScreen.route
        ),
        BottomBarItem(
            title = "Plus",
            icon = Icons.Default.PostAdd,
            iconContentDescription = "Ícone de plus",
            route = Screen.PlusScreen.route
        ),
        BottomBarItem(
            title = "Configurações",
            icon = Icons.Default.Settings,
            iconContentDescription = "Ícone de configurações",
            route = Screen.SettingsScreen.route
        ),
    )

    BottomAppBar {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            items(
                items = bottomBarItems,
                key = { it.route }
            ) { bottomBarItem ->
                val selected = bottomBarItem.route == actualScreen

                val offsetY: Dp by animateDpAsState(
                    targetValue = if (selected) 8.dp else 0.dp,
                    label = "offsetY"
                )

                Column(
                    modifier = Modifier
                        .offset { IntOffset(x = 0, y = -offsetY.roundToPx()) }
                        .clip(RoundedCornerShape(5.dp))
                        .clickable {
                            if(bottomBarItem.route != actualScreen) {
                                onNavigateToSelectedScreen(bottomBarItem.route)
                            }
                        }
                        .padding(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = bottomBarItem.icon,
                        contentDescription = bottomBarItem.iconContentDescription,
                        tint = if(selected) PrimaryColor else OnBackgroundColor
                    )

                    Text(
                        text = bottomBarItem.title,
                        color = if(selected) PrimaryColor else OnBackgroundColor
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun BottomBarPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        BottomBar(
            actualScreen = "Carteira",
            onNavigateToSelectedScreen = {}
        )
    }
}