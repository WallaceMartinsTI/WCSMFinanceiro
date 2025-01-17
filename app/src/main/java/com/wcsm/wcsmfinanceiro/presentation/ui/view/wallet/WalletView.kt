package com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wcsm.wcsmfinanceiro.R
import com.wcsm.wcsmfinanceiro.domain.entity.Account
import com.wcsm.wcsmfinanceiro.domain.entity.AccountCard
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.ErrorColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.GrayColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.MoneyGreenColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.TertiaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.util.toBrazilianReal

@Composable
fun WalletView(
    walletViewModel: WalletViewModel = viewModel()
) {
    val wallets by walletViewModel.wallets.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(BackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CARTEIRA",
                color = PrimaryColor,
                modifier = Modifier.padding(vertical = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                fontFamily = PoppinsFontFamily
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = wallets ?: emptyList(),
                    key = { wallet -> wallet.id }
                ) { wallet ->
                    AccountCardContainer(
                        account = wallet
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = {},
            modifier = Modifier.align(Alignment.BottomEnd),
            containerColor = PrimaryColor,
            contentColor = OnSecondaryColor
        ) {
            Icon(
                imageVector = Icons.Default.AddCard,
                contentDescription = "Ícone de adicionar cartão",
            )
        }
    }
}

@Preview
@Composable
private fun WalletViewPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        WalletView()
    }
}

@Composable
fun AccountCardContainer(account: Account) {
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
                    text = account.title,
                    color = SecondaryColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = account.balance.toBrazilianReal(),
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
                    items = account.accountCards,
                    key = { accountCards -> accountCards.id }
                ) { accountCard ->
                    CardContainer(card = accountCard)
                }
            }
        }
    }
}

@Preview
@Composable
private fun AccountCardContainerPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        val account = Account(
            id = 1,
            title = "Nubank",
            balance = 1725.74,
            accountCards = listOf(
                AccountCard(
                    id = 1,
                    title = "Cartão de Crédito",
                    total = 5000.00,
                    spent = 1500.00,
                    remaining = 3500.00
                )
            )
        )

        AccountCardContainer(account)
    }
}

@Composable
fun CardContainer(
    card: AccountCard,
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
                    text = "Gasto",
                    color = ErrorColor,
                    style = customTextStyle
                )
                Text(
                    text = "Total",
                    color = OnSurfaceColor,
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
                    text = card.spent.toBrazilianReal(),
                    color = ErrorColor,
                    style = customTextStyle
                )
                Text(
                    text = card.total.toBrazilianReal(),
                    color = OnSurfaceColor,
                    style = customTextStyle
                )
                Text(
                    text = card.remaining.toBrazilianReal(),
                    color = MoneyGreenColor,
                    style = customTextStyle
                )
            }
        }
    }
}

@Preview
@Composable
private fun CardContainerPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        val accountCard = AccountCard(
            id = 1,
            title = "Cartão de Crédito",
            total = 5000.00,
            spent = 1500.00,
            remaining = 3500.00
        )

        CardContainer(accountCard)
    }
}

private fun getRandomAccountCardContainerColor() : Color {
    val cardColors = listOf(
        Color(0xFF000957),
        Color(0xFF441752),
        Color(0xFF3D3D3D),
        Color(0xFF123524)
    )

    return cardColors.random()
}