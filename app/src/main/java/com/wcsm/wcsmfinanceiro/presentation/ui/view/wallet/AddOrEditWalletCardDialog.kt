package com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wcsm.wcsmfinanceiro.data.entity.Wallet
import com.wcsm.wcsmfinanceiro.presentation.model.UiState
import com.wcsm.wcsmfinanceiro.presentation.model.WalletCardState
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.ErrorColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color
import com.wcsm.wcsmfinanceiro.presentation.ui.view.bills.WalletDropdownChooser
import com.wcsm.wcsmfinanceiro.presentation.util.CurrencyVisualTransformation
import com.wcsm.wcsmfinanceiro.presentation.util.getDoubleForStringPrice
import com.wcsm.wcsmfinanceiro.presentation.util.toBrazilianReal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AddOrEditWalletCardDialog(
    walletsList: List<Wallet>,
    walletCardStateFlow: StateFlow<WalletCardState>,
    uiStateFlow: StateFlow<UiState>,
    onValueChange: (updatedValue: WalletCardState) -> Unit,
    onAddWalletCard: (walletCardState: WalletCardState) -> Unit,
    onUpdateWalletCard: (walletCardState: WalletCardState) -> Unit,
    onDeleteWalletCard: (walletCardState: WalletCardState) -> Unit,
    onDismiss: () -> Unit
) {
    val uiState by uiStateFlow.collectAsStateWithLifecycle()
    val walletCardDialogState by walletCardStateFlow.collectAsStateWithLifecycle()

    val isWalletCardToEdit by remember { mutableStateOf(walletCardDialogState.walletCardId != 0L) }

    var isModalLoading by remember { mutableStateOf(isWalletCardToEdit) }

    var monetaryValueLimit by remember { mutableStateOf("") }
    var monetaryValueSpent by remember { mutableStateOf("") }

    var availableAmount by remember { mutableDoubleStateOf(0.0) }

    LaunchedEffect(walletCardDialogState) {
        if(isWalletCardToEdit) {
            monetaryValueLimit = walletCardDialogState.limit.toString().replace(".", "")
            monetaryValueSpent = walletCardDialogState.spent.toString().replace(".", "")
            availableAmount = walletCardDialogState.available

            delay(1500)

            isModalLoading = false
        }
    }

    LaunchedEffect(uiState) {
        uiState.operationType?.let {
            if(uiState.success) {
                onDismiss()
            }
        }
    }

    LaunchedEffect(monetaryValueLimit) {
        if(monetaryValueLimit.isNotBlank()) {
            onValueChange(
                walletCardDialogState.copy(
                    limit = getDoubleForStringPrice(monetaryValueLimit),
                    available = (getDoubleForStringPrice(monetaryValueLimit) - getDoubleForStringPrice(monetaryValueSpent))
                )
            )
        }
    }

    LaunchedEffect(monetaryValueSpent) {
        if(monetaryValueSpent.isNotBlank()) {
            onValueChange(
                walletCardDialogState.copy(
                    spent = getDoubleForStringPrice(monetaryValueSpent),
                    available = (getDoubleForStringPrice(monetaryValueLimit) - getDoubleForStringPrice(monetaryValueSpent))
                )
            )
        }
    }

    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(SurfaceColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),//.padding(bottom = 8.dp),
            ) {
                Text(
                    //text = if (bill != null) "EDITAR CONTA" else "ADICIONAR CONTA",
                    text = "ADICIONAR CARTÃO", // ou Editar
                    modifier = Modifier.align(Alignment.Center).padding(end = 16.dp),
                    color = PrimaryColor,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )

                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Ícone de fechar",
                    tint = White06Color,
                    modifier = Modifier
                        .clickable { onDismiss() }
                        .align(Alignment.TopEnd)
                        .size(40.dp)
                )
            }

            WalletDropdownChooser(
                wallets = walletsList,
                isError = walletCardDialogState.walletIdErrorMessage.isNotEmpty(),
                errorMessage = walletCardDialogState.walletIdErrorMessage
            ) {  selectedWallet ->
                onValueChange(
                    walletCardDialogState.copy(
                        walletId = selectedWallet.walletId
                    )
                )
            }

            OutlinedTextField(
                value = walletCardDialogState.title,
                onValueChange = {
                    onValueChange(
                        walletCardDialogState.copy(
                            title = it
                        )
                    )
                },
                modifier = Modifier
                    .width(272.dp),
                //.focusRequester(focusRequester[0]),
                label = {
                    Text(
                        text = "Título do Cartão*",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = "Ícone de carrinho de compra",
                        tint = White06Color
                    )
                },
                trailingIcon = {
                    /*if (billModalState.origin.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Ícone de x",
                            modifier = Modifier
                                .clickable {
                                    billModalState = billModalState.copy(
                                        origin = ""
                                    )
                                    focusRequester[0].requestFocus()
                                },
                            tint = White06Color
                        )
                    }*/
                },
                singleLine = true,
                isError = walletCardDialogState.titleErrorMessage.isNotEmpty(),
                supportingText = {
                    if(walletCardDialogState.titleErrorMessage.isNotEmpty()) {
                        Text(
                            text = walletCardDialogState.titleErrorMessage,
                            fontFamily = PoppinsFontFamily
                        )
                    } else {
                        Text(
                            text = "Ex.: Cartão de Crédito",
                            color = White06Color
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
            )

            OutlinedTextField(
                value = monetaryValueLimit,
                onValueChange = {
                    monetaryValueLimit = it
                },
                modifier = Modifier
                    .width(272.dp),
                //.focusRequester(focusRequester[0]),
                label = {
                    Text(
                        text = "Limite do Cartão*",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = "Ícone de dinheiro",
                        tint = White06Color
                    )
                },
                trailingIcon = {
                    /*if (billModalState.origin.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Ícone de x",
                            modifier = Modifier
                                .clickable {
                                    billModalState = billModalState.copy(
                                        origin = ""
                                    )
                                    focusRequester[0].requestFocus()
                                },
                            tint = White06Color
                        )
                    }*/
                },
                singleLine = true,
                isError = walletCardDialogState.limitErrorMessage.isNotEmpty(),
                supportingText = {
                    if(walletCardDialogState.limitErrorMessage.isNotEmpty()) {
                        Text(
                            text = walletCardDialogState.limitErrorMessage,
                            fontFamily = PoppinsFontFamily
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = CurrencyVisualTransformation()
            )

            OutlinedTextField(
                value = monetaryValueSpent,
                onValueChange = {
                    monetaryValueSpent = it
                },
                modifier = Modifier
                    .width(272.dp),
                //.focusRequester(focusRequester[0]),
                label = {
                    Text(
                        text = "Gasto*",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                placeholder = {
                    Text(
                        text = "Digite o título da carteiraa"
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = "Ícone de dinheiro",
                        tint = White06Color
                    )
                },
                trailingIcon = {
                    /*if (billModalState.origin.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Ícone de x",
                            modifier = Modifier
                                .clickable {
                                    billModalState = billModalState.copy(
                                        origin = ""
                                    )
                                    focusRequester[0].requestFocus()
                                },
                            tint = White06Color
                        )
                    }*/
                },
                singleLine = true,
                isError = walletCardDialogState.spentErrorMessage.isNotEmpty(),
                supportingText = {
                    if(walletCardDialogState.spentErrorMessage.isNotEmpty()) {
                        Text(
                            text = walletCardDialogState.spentErrorMessage,
                            fontFamily = PoppinsFontFamily
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = CurrencyVisualTransformation()
            )

            OutlinedTextField(
                value = availableAmount.toBrazilianReal(),
                onValueChange = {},
                modifier = Modifier
                    .width(272.dp),
                //.focusRequester(focusRequester[0]),
                label = {
                    Text(
                        text = "Livre",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                placeholder = {
                    Text(
                        text = "Digite o título da carteiraa"
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = "Ícone de dinheiro",
                        tint = White06Color
                    )
                },
                readOnly = true,
                singleLine = true,
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ErrorColor
                    )
                ) {
                    Text(
                        text = "CANCELAR"
                    )
                }

                Button(
                    onClick = {
                        if(isWalletCardToEdit) {
                            // UPDATE WALLET CARD
                        } else {
                            // NEW WALLET CARD
                            onAddWalletCard(walletCardDialogState)
                        }
                    }
                ) {
                    Text(
                        text = "ADICIONAR"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AddOrEditWalletCardDialogPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        val walletViewModel: WalletViewModel = hiltViewModel()

        Column(
            modifier = Modifier.fillMaxSize().background(BackgroundColor)
        ) {
            AddOrEditWalletCardDialog(
                walletsList = emptyList(),
                walletCardStateFlow = walletViewModel.walletCardStateFlow,
                uiStateFlow = walletViewModel.uiState,
                onValueChange = {},
                onAddWalletCard = {},
                onUpdateWalletCard = {},
                onDeleteWalletCard = {},
                onDismiss = {}
            )
        }
    }
}