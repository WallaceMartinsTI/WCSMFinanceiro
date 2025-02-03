package com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Wallet
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wcsm.wcsmfinanceiro.data.entity.WalletCard
import com.wcsm.wcsmfinanceiro.presentation.model.UiState
import com.wcsm.wcsmfinanceiro.presentation.model.WalletOperationType
import com.wcsm.wcsmfinanceiro.presentation.model.WalletState
import com.wcsm.wcsmfinanceiro.presentation.model.WalletType
import com.wcsm.wcsmfinanceiro.presentation.ui.component.AppLoader
import com.wcsm.wcsmfinanceiro.presentation.ui.component.MonetaryInputField
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.ErrorColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color
import com.wcsm.wcsmfinanceiro.presentation.util.CurrencyVisualTransformation
import com.wcsm.wcsmfinanceiro.presentation.util.getDoubleForStringPrice
import com.wcsm.wcsmfinanceiro.presentation.util.formatMonetaryValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrEditWalletDialog(
    walletStateFlow: StateFlow<WalletState>,
    walletCards: List<WalletCard>,
    uiStateFlow: StateFlow<UiState<WalletOperationType>>,
    onValueChange: (updatedValue: WalletState) -> Unit,
    onAddWallet: (walletState: WalletState) -> Unit,
    onUpdateWallet: (walletState: WalletState) -> Unit,
    onDeleteWallet: (walletState: WalletState) -> Unit,
    onWalletCardClick: (card: WalletCard) -> Unit,
    onUpdateOrDeleteWalletCard: (walletId: Long) -> Unit,
    onDismiss: () -> Unit
) {
    val uiState by uiStateFlow.collectAsStateWithLifecycle()
    val walletDialogState by walletStateFlow.collectAsStateWithLifecycle()

    val isWalletToEdit by remember { mutableStateOf(walletDialogState.walletId != 0L) }

    var isModalLoading by remember { mutableStateOf(isWalletToEdit) }

    var showConfirmWalletDeletionDialog by remember { mutableStateOf(false) }

    var walletHasCards: Boolean? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        walletHasCards = walletCards.isNotEmpty()

        if(isWalletToEdit) {
            delay(1500)
            isModalLoading = false
        }
    }

    LaunchedEffect(uiState) {
        uiState.operationType?.let { walletOperationType ->
            if(uiState.success && walletOperationType.walletType == WalletType.WALLET) {
                onDismiss()
            }

            if(
                uiState.success &&
                walletOperationType.walletType == WalletType.WALLET_CARD &&
                (walletOperationType is WalletOperationType.Delete ||
                walletOperationType is WalletOperationType.Update)
            ) {
                onUpdateOrDeleteWalletCard(walletDialogState.walletId)
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
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            ) {
                Text(
                    text = if (isWalletToEdit) "EDITAR CARTEIRA" else "ADICIONAR CARTEIRA",
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

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
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
                        isError = walletDialogState.titleErrorMessage.isNotBlank(),
                        supportingText = {
                            if(walletDialogState.titleErrorMessage.isNotBlank()) {
                                Text(
                                    text = walletDialogState.titleErrorMessage,
                                    fontFamily = PoppinsFontFamily
                                )
                            } else {
                                Text(
                                    text = "Ex.: Nubank",
                                    color = White06Color
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                    )

                    Log.i("#-# TESTE #-#", "walletDialogState.balanceErrorMessage.isNotBlank(): ${walletDialogState.balanceErrorMessage.isNotBlank()}")
                    Log.i("#-# TESTE #-#", "walletDialogState.balanceErrorMessage: ${walletDialogState.balanceErrorMessage}")

                    MonetaryInputField(
                        label = "Saldo*",
                        alreadyExistsDoubleValue = isWalletToEdit,
                        alreadyDoubleValue = walletDialogState.balance,
                        isError = walletDialogState.balanceErrorMessage.isNotBlank(),
                        errorMessage = walletDialogState.balanceErrorMessage,
                        onMonetaryValueChange = { doubleMonetaryValue ->
                            onValueChange(
                                walletDialogState.copy(
                                    balance = doubleMonetaryValue
                                )
                            )
                        },
                        modifier = Modifier.width(280.dp)
                    )

                    if(walletHasCards == true) {
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

                            LazyRow(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(1.dp, White06Color, RoundedCornerShape(10.dp))
                                    .padding(8.dp)
                            ) {
                                /*items(walletCards) { walletCard ->
                                    WalletCardContainer(
                                        modifier = Modifier.scale(0.9f),
                                        card = walletCard,
                                        onCardClick = { onWalletCardClick(walletCard) }
                                    )
                                }*/
                                items(walletCards) { walletCard ->
                                    WalletCardContainer(
                                        modifier = Modifier.scale(0.9f),
                                        card = walletCard,
                                        onCardClick = { onWalletCardClick(walletCard) }
                                    )
                                }
                            }
                        }
                    }

                    Button(
                        onClick = {
                            if(isWalletToEdit) {
                                // UPDATE WALLET
                                onUpdateWallet(walletDialogState)
                            } else {
                                // NEW WALLET
                                onAddWallet(walletDialogState)
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
                            if (isWalletToEdit) {
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
                                text = if (isWalletToEdit) "ATUALIZAR CARTEIRA" else "SALVAR CARTEIRA",
                                color = Color.White,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }

                    if (isWalletToEdit) {
                        Button(
                            onClick = { showConfirmWalletDeletionDialog = true },
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
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Ícone de lixeira."
                                )

                                Text(
                                    text = "EXCLUIR CARTEIRA"
                                )
                            }

                        }
                    }
                }

                if (isModalLoading) {
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

                if(showConfirmWalletDeletionDialog) {
                    BasicAlertDialog(
                        onDismissRequest = { showConfirmWalletDeletionDialog = false },
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
                                text = "EXCLUIR CARTEIRA",
                                color = PrimaryColor,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Text(
                                text = "Tem certeza que deseja excluir a carteira: ${walletDialogState.title}",
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = {
                                        onDeleteWallet(walletDialogState)
                                    },
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = ErrorColor
                                    )
                                ) {
                                    Text("EXCLUIR")
                                }

                                Button(
                                    onClick = { showConfirmWalletDeletionDialog = false },
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                ) {
                                    Text("CANCELAR")
                                }
                            }
                        }
                    }
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
                walletCards = emptyList(),
                uiStateFlow = walletViewModel.uiState,
                onValueChange = {},
                onAddWallet = {},
                onUpdateWallet = {},
                onDeleteWallet = {},
                onWalletCardClick = {},
                onUpdateOrDeleteWalletCard = {},
                onDismiss = {}
            )
        }
    }
}
