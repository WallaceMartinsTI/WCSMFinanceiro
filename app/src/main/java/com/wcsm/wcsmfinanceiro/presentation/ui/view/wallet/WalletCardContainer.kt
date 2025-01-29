package com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wcsm.wcsmfinanceiro.data.entity.WalletCard
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.ErrorColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.GrayColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.MoneyGreenColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.TertiaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.util.toBrazilianReal

@Composable
fun WalletCardContainer(
    card: WalletCard,
    modifier: Modifier = Modifier
) {
    val customTextStyle = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )

    Column(
        modifier
            .clip(RoundedCornerShape(topStart = 15.dp, bottomEnd = 15.dp))
            .border(1.dp, Color.White, RoundedCornerShape(topStart = 15.dp, bottomEnd = 15.dp))
            .width(250.dp)
            .background(GrayColor)
            .padding(8.dp)
    ) {
        Text(
            text = card.title,
            color = TertiaryColor,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column {
                Text(
                    text = "Total",
                    color = OnSurfaceColor,
                    style = customTextStyle
                )
                Text(
                    text = "Gasto",
                    color = ErrorColor,
                    style = customTextStyle
                )
                Text(
                    text = "Restante",
                    color = MoneyGreenColor,
                    style = customTextStyle
                )
            }

            Column {
                Text(
                    text = card.limit.toBrazilianReal(),
                    color = OnSurfaceColor,
                    style = customTextStyle
                )
                Text(
                    text = card.spent.toBrazilianReal(),
                    color = ErrorColor,
                    style = customTextStyle
                )
                Text(
                    text = card.available.toBrazilianReal(),
                    color = MoneyGreenColor,
                    style = customTextStyle
                )
            }
        }
    }
}

@Preview
@Composable
private fun WalletCardContainerPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        val walletCard = WalletCard(
            walletCardId = 1,
            walletId = 1,
            title = "Cartão de Crédito",
            limit = 5000.00,
            spent = 1500.00,
            available = 3500.00,
            blocked = false
        )

        WalletCardContainer(walletCard)
    }
}