package com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wcsm.wcsmfinanceiro.data.local.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.local.entity.relation.WalletWithCards
import com.wcsm.wcsmfinanceiro.presentation.model.UiState
import com.wcsm.wcsmfinanceiro.presentation.model.wallet.WalletCardState
import com.wcsm.wcsmfinanceiro.presentation.model.wallet.WalletOperationType
import com.wcsm.wcsmfinanceiro.presentation.ui.component.AppLoader
import com.wcsm.wcsmfinanceiro.presentation.ui.component.XIcon
import com.wcsm.wcsmfinanceiro.presentation.ui.component.CustomCheckbox
import com.wcsm.wcsmfinanceiro.presentation.ui.component.MonetaryInputField
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.ErrorColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color
import com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet.WalletViewModel
import com.wcsm.wcsmfinanceiro.util.toBrazilianReal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrEditWalletCardDialog(
    walletWithCards: List<WalletWithCards>,
    walletCardStateFlow: StateFlow<WalletCardState>,
    uiStateFlow: StateFlow<UiState<WalletOperationType>>,
    onValueChange: (updatedValue: WalletCardState) -> Unit,
    onAddWalletCard: (walletCardState: WalletCardState) -> Unit,
    onUpdateWalletCard: (walletCardState: WalletCardState) -> Unit,
    onDeleteWalletCard: (walletCardState: WalletCardState) -> Unit,
    onDismiss: () -> Unit
) {
    val uiState by uiStateFlow.collectAsStateWithLifecycle()
    val walletCardDialogState by walletCardStateFlow.collectAsStateWithLifecycle()

    val focusRequester = remember { FocusRequester() }

    val isWalletCardToEdit by remember { mutableStateOf(walletCardDialogState.walletCardId != 0L) }

    var isModalLoading by remember { mutableStateOf(isWalletCardToEdit) }

    var showConfirmWalletCardDeletionDialog by remember { mutableStateOf(false) }

    var availableAmount by remember { mutableDoubleStateOf(0.0) }

    LaunchedEffect(Unit) {
        if(isWalletCardToEdit) {
            availableAmount = walletCardDialogState.available

            delay(1500)

            isModalLoading = false
        }
    }

    LaunchedEffect(walletCardDialogState.limit, walletCardDialogState.spent) {
        val limit = walletCardDialogState.limit
        val spent = walletCardDialogState.spent
        availableAmount = limit - spent
    }

    LaunchedEffect(availableAmount) {
        onValueChange(
            walletCardDialogState.copy(
                available = availableAmount
            )
        )
    }

    LaunchedEffect(uiState) {
        uiState.operationType?.let {
            if(uiState.success) {
                onDismiss()
            }
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if(isWalletCardToEdit) "EDITAR CARTÃO" else "ADICIONAR CARTÃO",
                    modifier = Modifier.align(Alignment.Center).padding(end = 16.dp),
                    color = PrimaryColor,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )

                XIcon(
                    modifier = Modifier.align(Alignment.CenterEnd).size(40.dp)
                ) {
                    onDismiss()
                }
            }

            if(!isWalletCardToEdit) {
                WalletDropdownChooser(
                    walletWithCards = walletWithCards,
                    isError = walletCardDialogState.walletIdErrorMessage.isNotBlank(),
                    errorMessage = walletCardDialogState.walletIdErrorMessage
                ) {  selectedWallet ->
                    onValueChange(
                        walletCardDialogState.copy(
                            walletId = selectedWallet.wallet.walletId
                        )
                    )
                }
            }

            OutlinedTextField(
                value = walletCardDialogState.title,
                onValueChange = { newValue ->
                    if(newValue.length <= 30) {
                        onValueChange(
                            walletCardDialogState.copy(
                                title = newValue
                            )
                        )
                    }
                },
                modifier = Modifier
                    .width(272.dp)
                    .focusRequester(focusRequester),
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
                    if(walletCardDialogState.title.isNotEmpty()) {
                        XIcon {
                            onValueChange(
                                walletCardDialogState.copy(
                                    title = ""
                                )
                            )
                            focusRequester.requestFocus()
                        }
                    }
                },
                singleLine = true,
                isError = walletCardDialogState.titleErrorMessage.isNotBlank(),
                supportingText = {
                    if(walletCardDialogState.titleErrorMessage.isNotBlank()) {
                        Text(
                            text = walletCardDialogState.titleErrorMessage,
                            fontFamily = PoppinsFontFamily
                        )
                    } else {
                        Text(
                            text = "Ex.: Cartão de Crédito",
                            color = White06Color,
                            fontFamily = PoppinsFontFamily
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
            )

            MonetaryInputField(
                label = "Limite do Cartão*",
                alreadyExistsDoubleValue = isWalletCardToEdit,
                alreadyDoubleValue = walletCardDialogState.limit,
                isError = walletCardDialogState.limitErrorMessage.isNotBlank(),
                errorMessage = walletCardDialogState.limitErrorMessage,
                onMonetaryValueChange = { doubleMonetaryValue ->
                    onValueChange(
                        walletCardDialogState.copy(
                            limit = doubleMonetaryValue
                        )
                    )
                },
                modifier = Modifier.width(272.dp)
            )

            MonetaryInputField(
                label = "Gasto*",
                alreadyExistsDoubleValue = isWalletCardToEdit,
                alreadyDoubleValue = walletCardDialogState.spent,
                isError = walletCardDialogState.spentErrorMessage.isNotBlank(),
                errorMessage = walletCardDialogState.spentErrorMessage,
                onMonetaryValueChange = { doubleMonetaryValue ->
                    onValueChange(
                        walletCardDialogState.copy(
                            spent = doubleMonetaryValue
                        )
                    )
                },
                modifier = Modifier.width(272.dp)
            )

            OutlinedTextField(
                value = availableAmount.toBrazilianReal(),
                onValueChange = {},
                modifier = Modifier
                    .width(272.dp),
                label = {
                    Text(
                        text = "Livre",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                placeholder = {
                    Text(
                        text = "Digite o título da carteira",
                        fontFamily = PoppinsFontFamily
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = "Ícone de dinheiro",
                        tint = White06Color
                    )
                },
                supportingText = {},
                enabled = false,
                readOnly = true,
                singleLine = true,
            )

            CustomCheckbox(
                checkboxText = "Bloqueado?",
                alreadyChecked = walletCardDialogState.blocked
            ) { isChecked ->
                onValueChange(
                    walletCardDialogState.copy(
                        blocked = isChecked
                    )
                )
            }

            if(walletCardDialogState.responseErrorMessage.isNotBlank()) {
                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Erro: ${walletCardDialogState.responseErrorMessage}",
                    color = ErrorColor,
                    fontFamily = PoppinsFontFamily,
                    modifier = Modifier.width(280.dp).padding(horizontal = 16.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if(isWalletCardToEdit) {
                        onUpdateWalletCard(walletCardDialogState)
                    } else {
                        onAddWalletCard(walletCardDialogState)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isWalletCardToEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Ícone de editar."
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Ícone de salvar."
                        )
                    }
                    Text(
                        text = if(isWalletCardToEdit) "ATUALIZAR CARTÃO" else "SALVAR CARTÃO",
                        color = Color.White,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            if(isWalletCardToEdit) {
                Button(
                    onClick = {
                        showConfirmWalletCardDeletionDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ErrorColor
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Ícone de cancelar."
                        )

                        Text(
                            text = "EXCLUIR CARTÃO",
                            fontFamily = PoppinsFontFamily,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }

        if(isModalLoading) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .background(SurfaceColor),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AppLoader(modifier = Modifier.size(80.dp))
            }
        }

        if(showConfirmWalletCardDeletionDialog) {
            BasicAlertDialog(
                onDismissRequest = { showConfirmWalletCardDeletionDialog = false },
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .border(1.dp, OnSurfaceColor, RoundedCornerShape(15.dp))
                    .background(SurfaceColor)
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "EXCLUIR CARTÃO",
                        color = PrimaryColor,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "Tem certeza que deseja excluir o cartão: ${walletCardDialogState.title}",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                onDeleteWalletCard(walletCardDialogState)
                            },
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ErrorColor
                            )
                        ) {
                            Text(
                                text = "EXCLUIR",
                                fontFamily = PoppinsFontFamily
                            )
                        }

                        Button(
                            onClick = { showConfirmWalletCardDeletionDialog = false },
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        ) {
                            Text(
                                text = "CANCELAR",
                                fontFamily = PoppinsFontFamily
                            )
                        }
                    }
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
                walletWithCards = emptyList(),
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