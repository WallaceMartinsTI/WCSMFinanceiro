package com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.wcsmfinanceiro.data.local.entity.Wallet
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletDropdownChooser(
    wallets: List<Wallet>,
    isError: Boolean,
    errorMessage: String,
    modifier: Modifier = Modifier,
    onValueSelected: (selectedWallet: Wallet) -> Unit
) {
    var wallet by remember { mutableStateOf("Selecione uma carteira") }

    var showWalletsDropdown by remember { mutableStateOf(false) }

    LaunchedEffect(wallet) {
        if (wallet.isNotBlank() && wallet != "Selecione uma carteira") {
            val selectedWallet = wallets.filter {
                it.title == wallet
            }[0]

            onValueSelected(selectedWallet)
        }
    }

    Box(
        modifier = modifier
    ) {
        ExposedDropdownMenuBox(
            expanded = showWalletsDropdown,
            onExpandedChange = { showWalletsDropdown = !showWalletsDropdown }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .width(272.dp),
                value = wallet,
                onValueChange = {
                    showWalletsDropdown = !showWalletsDropdown
                },
                label = {
                    Text(
                        text = "Carteira",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Wallet,
                        contentDescription = "Ícone de carteira",
                        tint = White06Color
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector =
                        if (showWalletsDropdown) Icons.Filled.KeyboardArrowUp
                        else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Ícone de seta para cima ou para baixo",
                        tint = White06Color
                    )
                },
                isError = isError,
                supportingText = {
                    if(errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            fontFamily = PoppinsFontFamily
                        )
                    }
                },
                readOnly = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.None)
            )

            ExposedDropdownMenu(
                expanded = showWalletsDropdown,
                onDismissRequest = { showWalletsDropdown = false }
            ) {
                wallets.forEach { selectedWallet ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = selectedWallet.title,
                                fontFamily = PoppinsFontFamily
                            )
                        },
                        onClick = {
                            wallet = selectedWallet.title
                            showWalletsDropdown = false
                        }
                    )
                }
            }
        }
    }

}

@Preview
@Composable
private fun WalletDropdownChooserPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        val wallets = listOf(
            Wallet(
                walletId = 1999999,
                title = "Nubank",
                balance = 1725.74
            ),
            Wallet(
                walletId = 2999999,
                title = "Inter",
                balance = 1725.74
            ),
            Wallet(
                walletId = 3999999,
                title = "Caixa",
                balance = 1725.74
            ),
            Wallet(
                walletId = 3999999,
                title = "NomeMuitoGrandeLim20",
                balance = 1725.74
            ),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WalletDropdownChooser(wallets, false, "") {}
        }
    }
}