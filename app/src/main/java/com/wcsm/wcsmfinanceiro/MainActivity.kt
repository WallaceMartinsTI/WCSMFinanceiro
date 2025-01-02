package com.wcsm.wcsmfinanceiro

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.wcsm.wcsmfinanceiro.ui.theme.WCSMFinanceiroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            WCSMFinanceiroTheme(dynamicColor = false) {
                TestColors()
            }
        }
    }
}

@Composable
fun TestColors() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Seja bem vindo!")

        TextField(
            value = "teste@gmail.com",
            onValueChange = {},
            label = { Text("Digite seu E-mail") }
        )

        TextField(
            value = "",
            onValueChange = {}
        )

        Row {
            Text("Deseja receber notificações?")
            Checkbox(
                checked = true,
                onCheckedChange = {}
            )
        }

        Row {
            Text("Deseja receber notificações?")
            Checkbox(
                checked = false,
                onCheckedChange = {}
            )
        }

        Button(onClick = {}) {
            Text("ENTRAR")
        }
    }
}

@Preview(name = "Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun TestColorsPreviewLight() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        TestColors()
    }
}

@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TestColorsPreviewDark() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        TestColors()
    }
}