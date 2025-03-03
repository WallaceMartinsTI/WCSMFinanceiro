package com.wcsm.wcsmfinanceiro.presentation.ui.view.plus

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wcsm.wcsmfinanceiro.presentation.model.plus.PlusOptionItem
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.GrayColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.TertiaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color
import com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.components.CurrencyConverterView
import com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.components.PlusItem

@Composable
fun PlusView() {
    var showCurrencyConverter by remember { mutableStateOf(false) }

    val plusOptions = listOf(
        PlusOptionItem(
            icon = Icons.Default.CurrencyExchange,
            label = "Conversor de Moedas"
        ) { showCurrencyConverter = true }
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

            LazyColumn {
                items(plusOptions) { plusOptionItem ->
                    PlusItem(
                        plusOptionItem = plusOptionItem,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }

        if(showCurrencyConverter) {
            CurrencyConverterView()
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