package com.wcsm.wcsmfinanceiro.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.wcsm.wcsmfinanceiro.presentation.navigation.AuthenticationNavigation
import com.wcsm.wcsmfinanceiro.presentation.navigation.MainNavigation
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            WCSMFinanceiroTheme(dynamicColor = false) {
                //AuthenticationNavigation()
                MainNavigation()
            }
        }
    }
}