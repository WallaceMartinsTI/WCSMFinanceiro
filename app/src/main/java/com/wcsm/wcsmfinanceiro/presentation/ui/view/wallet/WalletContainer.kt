package com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wcsm.wcsmfinanceiro.data.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.entity.WalletCard
import com.wcsm.wcsmfinanceiro.data.entity.relation.WalletWithCards
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.util.toBrazilianReal

@Composable
fun WalletContainer(
    walletsWithCards: WalletWithCards,
    modifier: Modifier = Modifier,
    onWalletClick: () -> Unit
) {
    ElevatedCard(
        onClick = { onWalletClick() }
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = walletsWithCards.wallet.title,
                    color = SecondaryColor,
                    fontSize = 24.sp,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PoppinsFontFamily,
                    modifier = Modifier.width(150.dp).padding(end = 8.dp)
                )

                Text(
                    text = walletsWithCards.wallet.balance.toBrazilianReal(),
                    fontFamily = PoppinsFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                )
            }

            if(walletsWithCards.walletCards.isEmpty()) {
                Text(
                    text = "Carteira sem cartões.",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(8.dp)
                )
            } else {
                LazyRow(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = walletsWithCards.walletCards,
                        key = { it.walletCardId }
                    ) { walletCard ->
                        WalletCardContainer(card = walletCard) {
                            onWalletClick()
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "With Cards")
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

        WalletContainer(walletWithCard) {}
    }
}

@Preview(name = "No Cards")
@Composable
private fun WalletContainerNoCardsPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        val walletWithCard = WalletWithCards(
            wallet = Wallet(
                walletId = 1,
                title = "Limite20caracteresjj",
                balance = 9999999.99
            ),
            walletCards = emptyList()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WalletContainer(walletWithCard) {}
        }
    }
}