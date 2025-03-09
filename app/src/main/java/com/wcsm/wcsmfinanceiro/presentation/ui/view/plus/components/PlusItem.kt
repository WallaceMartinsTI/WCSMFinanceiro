package com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.wcsmfinanceiro.presentation.model.plus.PlusOptionItem
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme

@Composable
fun PlusItem(
    plusOptionItem: PlusOptionItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White.copy(0.1f))
            .fillMaxWidth()
            .height(50.dp)
            .border(1.dp, PrimaryColor, RoundedCornerShape(15.dp))
            .clickable {
                plusOptionItem.onItemClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Icon(
            imageVector = plusOptionItem.icon,
            contentDescription = null,
            tint = PrimaryColor,
            modifier = Modifier.padding(horizontal = 16.dp).size(32.dp)
        )

        Text(
            text = plusOptionItem.label,
            color = PrimaryColor,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = PrimaryColor,
            modifier = Modifier.padding(horizontal = 16.dp).size(32.dp)
        )
    }
}

@Preview
@Composable
private fun PlusItemPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        val plusOptionItem = PlusOptionItem(
            icon = Icons.Default.CurrencyExchange,
            label = "Conversor de Moedas"
        ) {}

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PlusItem(plusOptionItem = plusOptionItem)
            PlusItem(plusOptionItem = plusOptionItem.copy(label = "Calculadora de Parcelas"))
            PlusItem(plusOptionItem = plusOptionItem.copy(label = "Assinaturas"))
        }
    }
}