package com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wcsm.wcsmfinanceiro.data.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.entity.WalletCard
import com.wcsm.wcsmfinanceiro.data.entity.relation.WalletWithCards
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.util.toBrazilianReal

@Composable
fun WalletContainer(
    walletsWithCards: WalletWithCards
) {
    ElevatedCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = walletsWithCards.wallet.title,
                    color = SecondaryColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = walletsWithCards.wallet.balance.toBrazilianReal(),
                    fontFamily = PoppinsFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            LazyRow(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = walletsWithCards.walletCards,
                    key = { walletCard -> walletCard.walletCardId }
                ) { walletCard ->
                    WalletCardContainer(card = walletCard)
                }
            }
        }
    }
}

@Preview
@Composable
private fun WalletContainerPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        val walletWithCard = WalletWithCards(
            wallet = Wallet(
                walletId = 1,
                title = "Nubank",
                balance = 1725.74
            ),
            walletCards = listOf(
                WalletCard(
                    walletCardId = 1,
                    walletId = 1,
                    title = "Cartão de Crédito",
                    limit = 5000.00,
                    spent = 1500.00,
                    available = 3500.00,
                    blocked = false
                ),
                WalletCard(
                    walletCardId = 2,
                    walletId = 1,
                    title = "Cartão Adicional",
                    limit = 5000.00,
                    spent = 1500.00,
                    available = 3500.00,
                    blocked = true
                )
            )
        )

        WalletContainer(walletWithCard)
    }
}