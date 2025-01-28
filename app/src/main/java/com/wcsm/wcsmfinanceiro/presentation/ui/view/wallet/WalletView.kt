package com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wcsm.wcsmfinanceiro.data.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.entity.WalletCard
import com.wcsm.wcsmfinanceiro.data.entity.relation.WalletWithCards
import com.wcsm.wcsmfinanceiro.presentation.model.OperationType
import com.wcsm.wcsmfinanceiro.presentation.ui.component.AppLoader
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
import com.wcsm.wcsmfinanceiro.presentation.util.showToastMessage
import com.wcsm.wcsmfinanceiro.presentation.util.toBrazilianReal
import com.wcsm.wcsmfinanceiro.presentation.util.toWalletState

@Composable
fun WalletView(
) {
    val walletViewModel: WalletViewModel = hiltViewModel()

    val context = LocalContext.current

    val walletsWithCards by walletViewModel.walletsWithCards.collectAsStateWithLifecycle()
    val uiState by walletViewModel.uiState.collectAsStateWithLifecycle()

    var showWalletAddChooserDialog by remember { mutableStateOf(false) }
    var showAddOrEditWalletDialog by remember { mutableStateOf(false) }
    var showAddOrEditWalletCardDialog by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(uiState.isLoading) }

    var walletsList: List<Wallet> by remember { mutableStateOf(emptyList()) }

    LaunchedEffect(walletsWithCards) {
        walletsList = walletsWithCards?.map {
            it.wallet
        } ?: emptyList()
    }

    LaunchedEffect(uiState) {
        isLoading = uiState.isLoading

        uiState.error?.let { errorMessage ->
            showToastMessage(context, errorMessage)
        }

        if(uiState.success) {
            uiState.operationType?.let { operationType ->
                when(operationType) {
                    OperationType.SAVE -> {
                        showToastMessage(context, "Carteira salva!")
                    }
                    OperationType.UPDATE -> {
                        showToastMessage(context, "Carteira atualizada!")
                    }
                    OperationType.DELETE -> {
                        showToastMessage(context, "Carteira removida!")
                    }
                }
            }

            walletViewModel.resetUiState()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        if(isLoading) {
            AppLoader(
                modifier = Modifier.size(80.dp).align(Alignment.Center)
            )
        } else {
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
                        items = walletsWithCards ?: emptyList(),
                        key = { walletsWithCards -> walletsWithCards.wallet.walletId }
                    ) { walletsWithCards ->
                        WalletContainer(
                            walletsWithCards = walletsWithCards
                        ) {
                            walletViewModel.updateWalletState(walletsWithCards.wallet.toWalletState())
                            showAddOrEditWalletDialog = true
                        }
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
                onAddWallet = { showAddOrEditWalletDialog = true },
                onAddCard = { showAddOrEditWalletCardDialog = true },
                onDismiss = { showWalletAddChooserDialog = false }
            )
        }
        
        if(showAddOrEditWalletDialog) {
            AddOrEditWalletDialog(
                walletStateFlow = walletViewModel.walletStateFlow,
                walletCardStateFlow = walletViewModel.walletCardStateFlow,
                uiStateFlow = walletViewModel.uiState,
                onValueChange = { updatedValue ->
                    walletViewModel.updateWalletState(updatedValue)
                },
                onAddWallet = { walletState ->
                    walletViewModel.saveWallet(walletState)
                },
                onUpdateWallet = {},
                onDeleteWallet = {},
                onDismiss = {
                    walletViewModel.resetWalletState()
                    walletViewModel.resetWalletCardState()
                    showAddOrEditWalletDialog = false
                }
            )
        }

        if(showAddOrEditWalletCardDialog) {
            AddOrEditWalletCardDialog(
                walletsList = walletsList,
                walletCardStateFlow = walletViewModel.walletCardStateFlow,
                uiStateFlow = walletViewModel.uiState,
                onValueChange = { updatedValue ->
                    walletViewModel.updateWalletCardState(updatedValue)
                },
                onAddWalletCard = { walletCardState ->
                    walletViewModel.saveWalletCard(walletCardState)
                },
                onUpdateWalletCard = {},
                onDeleteWalletCard = {},
                onDismiss = { showAddOrEditWalletCardDialog = false }
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