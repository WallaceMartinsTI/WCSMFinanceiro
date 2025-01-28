package com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wcsm.wcsmfinanceiro.data.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.entity.WalletCard
import com.wcsm.wcsmfinanceiro.presentation.model.UiState
import com.wcsm.wcsmfinanceiro.presentation.model.WalletState
import com.wcsm.wcsmfinanceiro.presentation.ui.component.AppLoader
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.ErrorColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color
import com.wcsm.wcsmfinanceiro.presentation.util.CurrencyVisualTransformation
import com.wcsm.wcsmfinanceiro.presentation.util.getDoubleForStringPrice
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AddOrEditWalletDialog(
    walletStateFlow: StateFlow<WalletState>,
    uiStateFlow: StateFlow<UiState>,
    onValueChange: (updatedValue: WalletState) -> Unit,
    onAddWallet: (walletState: WalletState) -> Unit,
    onUpdateWallet: (walletState: WalletState) -> Unit,
    onDeleteWallet: (walletState: WalletState) -> Unit,
    onDismiss: () -> Unit
) {
    val uiState by uiStateFlow.collectAsStateWithLifecycle()
    val walletDialogState by walletStateFlow.collectAsStateWithLifecycle()

    val isWalletToEdit by remember { mutableStateOf(walletDialogState.walletId != 0L) }

    var monetaryValue by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        uiState.operationType?.let {
            if(uiState.success) {
                onDismiss()
            }
        }
    }

    LaunchedEffect(monetaryValue) {
        if(monetaryValue.isNotBlank()) {
            onValueChange(
                walletDialogState.copy(
                    balance = getDoubleForStringPrice(monetaryValue)
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
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            ) {
                Text(
                    //text = if (bill != null) "EDITAR CONTA" else "ADICIONAR CONTA",
                    text = "ADICIONAR CARTEIRA", // ou Editar
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

            OutlinedTextField(
                value = walletDialogState.title,
                onValueChange = {
                    onValueChange(
                        walletDialogState.copy(
                            title = it
                        )
                    )
                },
                modifier = Modifier
                    .width(280.dp),
                    //.focusRequester(focusRequester[0]),
                label = {
                    Text(
                        text = "Título*",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                placeholder = {
                    Text(
                        text = "Digite o título da carteira"
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Wallet,
                        contentDescription = "Ícone de carteira",
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
                isError = walletDialogState.titleErrorMessage.isNotEmpty(),
                supportingText = {
                    if(walletDialogState.titleErrorMessage.isNotEmpty()) {
                        Text(
                            text = walletDialogState.titleErrorMessage,
                            fontFamily = PoppinsFontFamily
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
            )

            OutlinedTextField(
                value = monetaryValue,
                onValueChange = { newValue ->
                    monetaryValue = newValue
                },
                modifier = Modifier
                    .width(280.dp),
                //.focusRequester(focusRequester[0]),
                label = {
                    Text(
                        text = "Saldo*",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                placeholder = {
                    Text(
                        text = "Digite o valor que você tem nesta carteira"
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
                isError = walletDialogState.balanceErrorMessage.isNotEmpty(),
                supportingText = {
                    if(walletDialogState.balanceErrorMessage.isNotEmpty()) {
                        Text(
                            text = walletDialogState.balanceErrorMessage,
                            fontFamily = PoppinsFontFamily
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                visualTransformation = CurrencyVisualTransformation()
            )

            Column(
               modifier =  Modifier
                   .width(280.dp)
                   .padding(8.dp),
               horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "CARTÕES",
                    color = PrimaryColor,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

                val walletCard = WalletCard(
                    walletId = 1,
                    walletCardId = 1,
                    title = "Cartão de Crédito",
                    limit = 5000.00,
                    spent = 1500.00,
                    available = 3500.00,
                    blocked = false
                )

                // COLOCAR ONCLICK NO CARTAO PARA AO CLICAR ABRIR O DIALOG
                // DE ADD/EDIT WalletCard e permitir edição e exclusão
                LazyRow(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, White06Color, RoundedCornerShape(10.dp))
                        .padding(8.dp)
                ) {
                    items(4) {
                        WalletCardContainer(
                            modifier = Modifier.scale(0.9f),
                            card = walletCard
                        )
                    }
                }
            }

            //Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if(isWalletToEdit) {
                        // UPDATE WALLET
                    } else {
                        // NEW WALLET
                        onAddWallet(walletDialogState)
                    }
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    /*if(isBillToEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Ícone de editar."
                        )
                    } else {*/
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Ícone de salvar."
                        )
                    //}

                    Text(
                        //text = if (isBillToEdit) "ATUALIZAR" else "SALVAR",
                        text = "SALVAR",
                        color = Color.White,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun AddOrEditWalletDialogPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        val walletViewModel: WalletViewModel = hiltViewModel()
        Column(
            modifier = Modifier.fillMaxSize().background(BackgroundColor)
        ) {
            AddOrEditWalletDialog(
                walletStateFlow = walletViewModel.walletStateFlow,
                uiStateFlow = walletViewModel.uiState,
                onValueChange = {},
                onAddWallet = {},
                onUpdateWallet = {},
                onDeleteWallet = {},
                onDismiss = {}
            )
        }
    }
}
