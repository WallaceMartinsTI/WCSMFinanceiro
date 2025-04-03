package com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wcsm.wcsmfinanceiro.data.local.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.local.entity.WalletCard
import com.wcsm.wcsmfinanceiro.presentation.model.wallet.WalletOperationType
import com.wcsm.wcsmfinanceiro.presentation.model.wallet.WalletType
import com.wcsm.wcsmfinanceiro.presentation.ui.component.AppLoader
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet.components.AddOrEditWalletCardDialog
import com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet.components.AddOrEditWalletDialog
import com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet.components.WalletAddChooser
import com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet.components.WalletContainer
import com.wcsm.wcsmfinanceiro.util.showToastMessage
import com.wcsm.wcsmfinanceiro.util.toWalletCardState
import com.wcsm.wcsmfinanceiro.util.toWalletState

@Composable
fun WalletView(
    walletViewModel: WalletViewModel
) {
    val context = LocalContext.current

    val walletsWithCards by walletViewModel.walletsWithCards.collectAsStateWithLifecycle()
    val uiState by walletViewModel.uiState.collectAsStateWithLifecycle()

    val walletsWithCardsList = remember(walletsWithCards) { walletsWithCards ?: emptyList() }

    var showWalletAddChooserDialog by remember { mutableStateOf(false) }
    var showAddOrEditWalletDialog by remember { mutableStateOf(false) }
    var showAddOrEditWalletCardDialog by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(uiState.isLoading) }

    var walletCards: List<WalletCard> by remember { mutableStateOf(emptyList()) }

    var walletsList: List<Wallet> by remember { mutableStateOf(emptyList()) }

    LaunchedEffect(Unit) {
        walletViewModel.getWalletWithCards()
    }

    LaunchedEffect(walletsWithCards) {
        walletsList = walletsWithCards?.map {
            it.wallet
        } ?: emptyList()
    }

    LaunchedEffect(uiState) {
        isLoading = uiState.isLoading

        uiState.error?.let { errorMessage ->
            if(errorMessage.isNotBlank() && uiState.operationType != null) {
                when(uiState.operationType!!.walletType) {
                    WalletType.WALLET -> {
                        walletViewModel.updateWalletState(
                            walletViewModel.walletStateFlow.value.copy(
                                responseErrorMessage = errorMessage
                            )
                        )
                    }
                    WalletType.WALLET_CARD -> {
                        walletViewModel.updateWalletCardState(
                            walletViewModel.walletCardStateFlow.value.copy(
                                responseErrorMessage = errorMessage
                            )
                        )
                    }
                }
            }
        }

        if(uiState.success) {
            uiState.operationType?.let { operationType ->
                val message = getIdealSuccessMessage(operationType)
                when(operationType) {
                    is WalletOperationType.Save -> {
                        showToastMessage(context, message)
                    }
                    is WalletOperationType.Update -> {
                        showToastMessage(context, message)
                    }
                    is WalletOperationType.Delete -> {
                        showToastMessage(context, message)
                    }
                }
            }

            walletViewModel.resetUiState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CARTEIRAS",
            color = PrimaryColor,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp, start = 8.dp, end = 8.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 40.sp,
            fontFamily = PoppinsFontFamily
        )

        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            if(isLoading) {
                AppLoader(
                    modifier = Modifier.size(80.dp).align(Alignment.Center)
                )
            } else {
                if(walletsWithCardsList.isEmpty()) {
                    Text(
                        text = "Sem carteiras no momento.",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = walletsWithCardsList,
                            key = { it.wallet.walletId }
                        ) { walletsWithCards ->
                            WalletContainer(
                                walletsWithCards = walletsWithCards,
                                onWalletClick = {
                                    walletViewModel.updateWalletState(walletsWithCards.wallet.toWalletState())
                                    walletCards = walletsWithCards.walletCards
                                    showAddOrEditWalletDialog = true
                                }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(60.dp))
                        }
                    }
                }

                FloatingActionButton(
                    onClick = {
                        showWalletAddChooserDialog = true
                    },
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

            if(showWalletAddChooserDialog) {
                WalletAddChooser(
                    createCardAllowed = walletsList.isNotEmpty(),
                    onAddWallet = { showAddOrEditWalletDialog = true },
                    onAddCard = { showAddOrEditWalletCardDialog = true },
                    onDismiss = { showWalletAddChooserDialog = false }
                )
            }

            if(showAddOrEditWalletDialog) {
                AddOrEditWalletDialog(
                    walletStateFlow = walletViewModel.walletStateFlow,
                    walletCards = walletCards,
                    uiStateFlow = walletViewModel.uiState,
                    onValueChange = { updatedValue ->
                        walletViewModel.updateWalletState(updatedValue)
                    },
                    onAddWallet = { walletState ->
                        walletViewModel.saveWallet(walletState)
                    },
                    onUpdateWallet = { walletState ->
                        walletViewModel.updateWallet(walletState)
                    },
                    onDeleteWallet = { walletState ->
                        walletViewModel.deleteWallet(walletState)
                    },
                    onWalletCardClick = { walletCard ->
                        walletViewModel.updateWalletCardState(walletCard.toWalletCardState())
                        showAddOrEditWalletCardDialog = true
                    },
                    onUpdateOrDeleteWalletCard = { walletId ->
                        walletCards = walletViewModel.getWalletCardsByWallet(walletId)
                    },
                    onDismiss = {
                        walletViewModel.resetWalletState()
                        walletViewModel.resetWalletCardState()

                        walletCards = emptyList()

                        showAddOrEditWalletDialog = false
                    }
                )
            }

            if(showAddOrEditWalletCardDialog) {
                AddOrEditWalletCardDialog(
                    walletWithCards = walletsWithCards ?: emptyList(),
                    walletCardStateFlow = walletViewModel.walletCardStateFlow,
                    uiStateFlow = walletViewModel.uiState,
                    onValueChange = { updatedValue ->
                        walletViewModel.updateWalletCardState(updatedValue)
                    },
                    onAddWalletCard = { walletCardState ->
                        walletViewModel.saveWalletCard(walletCardState)
                    },
                    onUpdateWalletCard = { walletCardState ->
                        walletViewModel.updateWalletCard(walletCardState)
                    },
                    onDeleteWalletCard = { walletCardState ->
                        walletViewModel.deleteWalletCard(walletCardState)
                    },
                    onDismiss = { showAddOrEditWalletCardDialog = false }
                )
            }
        }
    }
}

@Preview
@Composable
private fun WalletViewPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        WalletView(hiltViewModel())
    }
}

private fun getIdealSuccessMessage(
    operationType: WalletOperationType,
) : String {
    val message = if(operationType.walletType == WalletType.WALLET) "Carteira" else "Cartão"
    val messageFinalLetter = if(operationType.walletType == WalletType.WALLET) "a" else "o"

    return when(operationType) {
        is WalletOperationType.Save -> {
            "$message salv$messageFinalLetter!"
        }
        is WalletOperationType.Update -> {
            "$message atualizad$messageFinalLetter!"
        }
        is WalletOperationType.Delete -> {
            "$message removid$messageFinalLetter!"
        }
    }
}