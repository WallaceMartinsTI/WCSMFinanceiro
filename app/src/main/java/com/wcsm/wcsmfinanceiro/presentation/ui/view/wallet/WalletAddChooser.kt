package com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.GrayColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme

@Composable
fun WalletAddChooser(
    createCardAllowed: Boolean,
    onAddWallet: () -> Unit,
    onAddCard: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(SurfaceColor)
                .padding(horizontal = 8.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .clickable {
                        onAddWallet()
                        onDismiss()
                    }
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
                    .background(PrimaryColor)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Wallet,
                    contentDescription = "Ícone de carteira.",
                    tint = Color.White,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Text(
                    text = "ADICIONAR CARTEIRA",
                    color = Color.White,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(8.dp))

            val backgroundColor = if(createCardAllowed) PrimaryColor else GrayColor
            val contentColor = if(createCardAllowed) Color.White else Color.White.copy(alpha = 0.6f)
            Row(
                modifier = Modifier
                    .clickable {
                        if(createCardAllowed) {
                            onAddCard()
                            onDismiss()
                        }
                    }
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
                    .background(backgroundColor)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = "Ícone de cartão.",
                    tint = contentColor,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Text(
                    text = "ADICIONAR CARTÃO",
                    color = contentColor,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(name = "Create card ALLOWED")
@Composable
private fun WalletAddChooserAllowedPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        WalletAddChooser(
            createCardAllowed = true,
            onAddWallet = {},
            onAddCard = {},
            onDismiss = {}
        )
    }
}

@Preview(name = "Create card NOT ALLOWED")
@Composable
private fun WalletAddChooserNotAllowedPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        WalletAddChooser(
            createCardAllowed = false,
            onAddWallet = {},
            onAddCard = {},
            onDismiss = {}
        )
    }
}