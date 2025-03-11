package com.wcsm.wcsmfinanceiro.presentation.ui.view.plus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wcsm.wcsmfinanceiro.presentation.model.plus.PlusOptionItem
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.components.CurrencyConverterView
import com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.components.InstallmentCalculatorView
import com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.components.PlusItem
import com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.components.SubscriptionsView

@Composable
fun PlusView() {
    val configuration = LocalConfiguration.current

    var showCurrencyConverterView by remember { mutableStateOf(false) }
    var showInstallmentCalculatorView by remember { mutableStateOf(false) }
    var showSubscriptionsView by remember { mutableStateOf(false) }

    val plusOptions = listOf(
        PlusOptionItem(
            icon = Icons.Default.CurrencyExchange,
            label = "Conversor de Moedas"
        ) { showCurrencyConverterView = true },
        PlusOptionItem(
            icon = Icons.Default.Calculate,
            label = "Calculadora de Parcelas"
        ) { showInstallmentCalculatorView = true },
        PlusOptionItem(
            icon = Icons.Default.Subscriptions,
            label = "Assinaturas"
        ) { showSubscriptionsView = true }
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(BackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "PLUS",
                color = PrimaryColor,
                modifier = Modifier.padding(vertical = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                fontFamily = PoppinsFontFamily
            )

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(plusOptions) { plusOptionItem ->
                    PlusItem(
                        plusOptionItem = plusOptionItem,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }

        if(showCurrencyConverterView) {
            CurrencyConverterView { showCurrencyConverterView = false}
        }

        if(showInstallmentCalculatorView) {
            InstallmentCalculatorView { showInstallmentCalculatorView = false }
        }

        if(showSubscriptionsView) {
            SubscriptionsView { showSubscriptionsView = false }
        }
    }
}



@Preview
@Composable
private fun LoginViewPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        PlusView()
    }
}