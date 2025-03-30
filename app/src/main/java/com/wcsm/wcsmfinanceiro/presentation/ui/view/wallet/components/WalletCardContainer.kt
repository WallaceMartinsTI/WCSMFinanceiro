package com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wcsm.wcsmfinanceiro.data.local.entity.WalletCard
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.ErrorColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.GrayColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.MoneyGreenColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.TertiaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.util.toBrazilianReal

@Composable
fun WalletCardContainer(
    card: WalletCard,
    modifier: Modifier = Modifier,
    onCardClick: (card: WalletCard) -> Unit
) {
    val customTextStyle = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )

    Column(
        modifier
            .clickable { onCardClick(card) }
            .clip(RoundedCornerShape(topStart = 15.dp, bottomEnd = 15.dp))
            .border(1.dp, Color.White, RoundedCornerShape(topStart = 15.dp, bottomEnd = 15.dp))
            .width(250.dp)
            .background(GrayColor)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = card.title,
                color = TertiaryColor,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            if(card.blocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Ícone de cadeado",
                    tint = SecondaryColor
                )
            }
        }

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

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            WalletCardContainer(
                card = walletCard.copy(
                    title = "Limite20Caractereskk",
                    limit = 9999999.99,
                    spent = 9999999.99,
                    available = 9999999.99
                ),
                onCardClick = {}
            )

            Spacer(Modifier.height(16.dp))

            WalletCardContainer(
                card = walletCard,
                onCardClick = {}
            )

            Spacer(Modifier.height(16.dp))

            WalletCardContainer(
                card = walletCard.copy(
                    title = "Cartão",
                    limit = 99999.99,
                    spent = 99999.99,
                    available = 99999.99,
                    blocked = true
                ),
                onCardClick = {}
            )
        }
    }
}