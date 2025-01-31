package com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wcsm.wcsmfinanceiro.data.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.entity.WalletCard
import com.wcsm.wcsmfinanceiro.data.entity.relation.WalletWithCards
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.DeleteWalletCardUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.DeleteWalletUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.GetWalletWithCardsUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.SaveWalletCardUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.SaveWalletUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.UpdateWalletCardUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.UpdateWalletUseCase
import com.wcsm.wcsmfinanceiro.presentation.model.WalletOperationType
import com.wcsm.wcsmfinanceiro.presentation.model.UiState
import com.wcsm.wcsmfinanceiro.presentation.model.WalletCardState
import com.wcsm.wcsmfinanceiro.presentation.model.WalletState
import com.wcsm.wcsmfinanceiro.presentation.model.WalletType
import com.wcsm.wcsmfinanceiro.presentation.util.toWallet
import com.wcsm.wcsmfinanceiro.presentation.util.toWalletCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val getWalletWithCardsUseCase: GetWalletWithCardsUseCase,
    private val saveWalletUseCase: SaveWalletUseCase,
    private val updateWalletUseCase: UpdateWalletUseCase,
    private val deleteWalletUseCase: DeleteWalletUseCase,
    private val saveWalletCardUseCase: SaveWalletCardUseCase,
    private val updateWalletCardUseCase: UpdateWalletCardUseCase,
    private val deleteWalletCardUseCase: DeleteWalletCardUseCase
) : ViewModel() {
    private val accountsLists = listOf(
        WalletWithCards(
            wallet = Wallet(
                walletId = 1999999,
                title = "Nubank",
                balance = 1725.74
            ),
            walletCards = listOf(
                WalletCard(
                    walletCardId = 1999999,
                    walletId = 1999999,
                    title = "Cartão de Crédito",
                    limit = 5000.00,
                    spent = 1500.00,
                    available = 3500.00,
                    blocked = false
                ),
                WalletCard(
                    walletCardId = 2999999,
                    walletId = 1999999,
                    title = "Cartão Adicional",
                    limit = 5000.00,
                    spent = 1500.00,
                    available = 3500.00,
                    blocked = false
                )
            )
        ),
        WalletWithCards(
            wallet = Wallet(
                walletId = 2999999,
                title = "Inter",
                balance = 1725.74
            ),
            walletCards = listOf(
                WalletCard(
                    walletCardId = 3999999,
                    walletId = 2999999,
                    title = "Cartão de Crédito",
                    limit = 5000.00,
                    spent = 1500.00,
                    available = 3500.00,
                    blocked = true
                )
            )
        ),
        WalletWithCards(
            wallet = Wallet(
                walletId = 3999999,
                title = "Caixa",
                balance = 1725.74
            ),
            walletCards = listOf(
                WalletCard(
                    walletCardId = 4999999,
                    walletId = 3999999,
                    title = "Cartão de Crédito",
                    limit = 5000.00,
                    spent = 1500.00,
                    available = 3500.00,
                    blocked = false
                ),
                WalletCard(
                    walletCardId = 5999999,
                    walletId = 3999999,
                    title = "Cartão Adicional",
                    limit = 5000.00,
                    spent = 1500.00,
                    available = 3500.00,
                    blocked = true
                )
                ,
                WalletCard(
                    walletCardId = 6999999,
                    walletId = 3999999,
                    title = "Teste",
                    limit = 5000.00,
                    spent = 1500.00,
                    available = 3500.00,
                    blocked = false
                )
            )
        ),
        WalletWithCards(
            wallet = Wallet(
                walletId = 4999999,
                title = "Sicoob",
                balance = 1725.74
            ),
            walletCards = listOf(
                WalletCard(
                    walletCardId = 7999999,
                    walletId = 4999999,
                    title = "Cartão de Crédito",
                    limit = 5000.00,
                    spent = 1500.00,
                    available = 3500.00,
                    blocked = false
                )
            )
        ),
        WalletWithCards(
            wallet = Wallet(
                walletId = 5999999,
                title = "Itaú",
                balance = 1725.74
            ),
            walletCards = listOf(
                WalletCard(
                    walletCardId = 8999999,
                    walletId = 5999999,
                    title = "Cartão de Crédito",
                    limit = 5000.00,
                    spent = 1500.00,
                    available = 3500.00,
                    blocked = false
                )
            )
        )
    )

    private val _uiState = MutableStateFlow(UiState<WalletOperationType>())
    val uiState = _uiState.asStateFlow()

    private val _walletStateFlow = MutableStateFlow(WalletState())
    val walletStateFlow = _walletStateFlow.asStateFlow()

    private val _walletCardStateFlow = MutableStateFlow(WalletCardState())
    val walletCardStateFlow = _walletCardStateFlow.asStateFlow()

    private val _walletsWithCards = MutableStateFlow<List<WalletWithCards>?>(null)
    val walletsWithCards = _walletsWithCards.asStateFlow()

    init {
        getWalletWithCards()
    }

    fun updateWalletState(updatedState: WalletState) {
        _walletStateFlow.value = updatedState
    }

    fun updateWalletCardState(updatedState: WalletCardState) {
        _walletCardStateFlow.value = updatedState
    }

    fun resetWalletState() {
        _walletStateFlow.value = WalletState()
    }

    fun resetWalletCardState() {
        _walletCardStateFlow.value = WalletCardState()
    }

    fun resetUiState() {
        _uiState.value = UiState()
    }

    fun getWalletWithCards() {
        viewModelScope.launch(Dispatchers.IO) {
            getWalletWithCardsUseCase().collect { result ->
                when(result) {
                    is Response.Loading -> {
                        _uiState.value = uiState.value.copy(
                            isLoading = true
                        )
                    }
                    is Response.Error -> {
                        // SHOW ERROR MESSAGE result.message
                        _uiState.value = uiState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                    is Response.Success -> {
                        _walletsWithCards.value = result.data.reversed() + accountsLists

                        _uiState.value = uiState.value.copy(
                            isLoading = false,
                            success = true
                        )
                    }
                }
            }
        }
    }

    fun saveWallet(walletState: WalletState) {
        resetWalletStateErrorMessages()

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(
                operationType = WalletOperationType.Save(WalletType.WALLET)
            )

            if(isWalletStateValid()) {
                val wallet = walletState.toWallet()
                saveWalletUseCase(wallet).collect { result ->
                    when(result) {
                        is Response.Loading -> {
                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                            )
                        }
                        is Response.Error -> {
                            // SHOW ERROR MESSAGE result.message
                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                        is Response.Success -> {
                            _walletStateFlow.value = WalletState()

                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                                success = true
                            )

                            getWalletWithCards()
                        }
                    }
                }
            }
        }
    }

    fun updateWallet(walletState: WalletState) {
        resetWalletStateErrorMessages()

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(
                operationType = WalletOperationType.Update(WalletType.WALLET)
            )

            if(isWalletStateValid()) {
                val wallet = walletState.toWallet()
                updateWalletUseCase(wallet).collect { result ->
                    when(result) {
                        is Response.Loading -> {
                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                            )
                        }
                        is Response.Error -> {
                            // SHOW ERROR MESSAGE result.message
                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                        is Response.Success -> {
                            _walletStateFlow.value = WalletState()

                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                                success = true
                            )

                            getWalletWithCards()
                        }
                    }
                }
            }
        }
    }

    fun deleteWallet(walletState: WalletState) {
        resetWalletStateErrorMessages()

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(
                operationType = WalletOperationType.Delete(WalletType.WALLET)
            )

            if(isWalletStateValid()) {
                val wallet = walletState.toWallet()
                deleteWalletUseCase(wallet).collect { result ->
                    when(result) {
                        is Response.Loading -> {
                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                            )
                        }
                        is Response.Error -> {
                            // SHOW ERROR MESSAGE result.message
                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                        is Response.Success -> {
                            _walletStateFlow.value = WalletState()

                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                                success = true
                            )

                            getWalletWithCards()
                        }
                    }
                }
            }
        }
    }

    private fun resetWalletStateErrorMessages() {
        updateWalletState(
            walletStateFlow.value.copy(
                titleErrorMessage = "",
                balanceErrorMessage = ""
            )
        )
    }

    private fun isWalletStateValid(): Boolean {
        val isTitleValid = validateWalletTitle(walletStateFlow.value.title)
        val isBalanceValid = validateWalletBalance(walletStateFlow.value.balance)

        updateWalletState(
            walletStateFlow.value.copy(
                titleErrorMessage = isTitleValid.second,
                balanceErrorMessage = isBalanceValid.second
            )
        )

        return isTitleValid.first && isBalanceValid.first
    }

    private fun validateWalletTitle(title: String): Pair<Boolean, String> {
        return if(title.isBlank()) {
            Pair(false, "O título não pode ser vazio")
        } else if(title.length < 3) {
            Pair(false, "O título é muito curto (min. 3 caracteres)")
        } else {
            Pair(true, "")
        }
    }

    private fun validateWalletBalance(balance: Double): Pair<Boolean, String> {
        return if(balance == 0.0) {
            Pair(false, "Você deve informar um valor maior que 0.")
        } else if(balance < 0) {
            Pair(false, "Valor inválido.")
        } else {
            Pair(true, "")
        }
    }

    // WALLET CARD SECTION
    fun saveWalletCard(walletCardState: WalletCardState) {
        resetWalletCardStateErrorMessages()

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(
                operationType = WalletOperationType.Save(WalletType.WALLET_CARD)
            )

            if(isWalletCardStateValid()) {
                val walletCard = walletCardState.toWalletCard()
                saveWalletCardUseCase(walletCard).collect { result ->
                    when(result) {
                        is Response.Loading -> {
                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                            )
                        }
                        is Response.Error -> {
                            // SHOW ERROR MESSAGE result.message
                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                        is Response.Success -> {
                            _walletCardStateFlow.value = WalletCardState()

                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                                success = true
                            )

                            getWalletWithCards()
                        }
                    }
                }
            }
        }
    }

    fun updateWalletCard(walletCardState: WalletCardState) {
        resetWalletCardStateErrorMessages()

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(
                operationType = WalletOperationType.Update(WalletType.WALLET_CARD)
            )

            if(isWalletCardStateValid()) {
                val walletCard = walletCardState.toWalletCard()
                updateWalletCardUseCase(walletCard).collect { result ->
                    when(result) {
                        is Response.Loading -> {
                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                            )
                        }
                        is Response.Error -> {
                            // SHOW ERROR MESSAGE result.message
                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                        is Response.Success -> {
                            _walletCardStateFlow.value = WalletCardState()

                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                                success = true
                            )

                            getWalletWithCards()
                        }
                    }
                }
            }
        }
    }

    fun deleteWalletCard(walletCardState: WalletCardState) {
        resetWalletCardStateErrorMessages()

        Log.i("#-# TESTE #-#", "Chamou VIEWMODEL deleteWalletCard")
        Log.i("#-# TESTE #-#", "walletCardState: $walletCardState")
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(
                operationType = WalletOperationType.Delete(WalletType.WALLET_CARD)
            )

            if(isWalletCardStateValid()) {
                val walletCard = walletCardState.toWalletCard()
                Log.i("#-# TESTE #-#", "isValid e walletCard: $walletCard")
                deleteWalletCardUseCase(walletCard).collect { result ->
                    Log.i("#-# TESTE #-#", "result: $result")
                    when(result) {
                        is Response.Loading -> {
                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                            )
                        }
                        is Response.Error -> {
                            // SHOW ERROR MESSAGE result.message
                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                        is Response.Success -> {
                            _walletCardStateFlow.value = WalletCardState()

                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                                success = true
                            )

                            getWalletWithCards()
                        }
                    }
                }
            }
        }
    }

    private fun resetWalletCardStateErrorMessages() {
        updateWalletCardState(
            walletCardStateFlow.value.copy(
                titleErrorMessage = "",
                limitErrorMessage = "",
                spentErrorMessage = ""
            )
        )
    }

    private fun isWalletCardStateValid(): Boolean {
        val isTitleValid = validateWalletCardTitle(walletCardStateFlow.value.title)
        val isLimitValid = validateWalletCardLimit(walletCardStateFlow.value.limit)
        val isSpentValid = validateWalletCardSpent(walletCardStateFlow.value.spent)
        val isWalletIdValid = validateWalletIdForCreateWalletCard(walletCardStateFlow.value.walletId)

        updateWalletCardState(
            walletCardStateFlow.value.copy(
                titleErrorMessage = isTitleValid.second,
                limitErrorMessage = isLimitValid.second,
                spentErrorMessage = isSpentValid.second,
                walletIdErrorMessage = isWalletIdValid.second
            )
        )

        return isTitleValid.first && isLimitValid.first && isSpentValid.first && isWalletIdValid.first
    }

    private fun validateWalletIdForCreateWalletCard(walletId: Long): Pair<Boolean, String> {
        if(walletId == 0L) {
            return Pair(false, "Você deve selecionar uma carteira.")
        }

        walletsWithCards.value?.map {
            it.wallet
        }?.let {
            val walletIdExist = it.filter { wallet ->
                wallet.walletId == walletId
            }

            if(walletIdExist.size == 1) {
                return Pair(true, "")
            }
        }

        return Pair(false, "Erro desconhecido, informe o administrador.")
    }

    private fun validateWalletCardTitle(title: String): Pair<Boolean, String> {
        return if(title.isBlank()) {
            Pair(false, "O título não pode ser vazio.")
        } else if(title.length < 3) {
            Pair(false, "O título é muito curto (min. 3 caracteres).")
        } else {
            Pair(true, "")
        }
    }

    private fun validateWalletCardLimit(limit: Double): Pair<Boolean, String> {
        return if(limit == 0.0) {
            Pair(false, "Você deve informar um valor maior que 0.")
        } else if(limit < 0) {
            Pair(false, "Valor inválido.")
        } else {
            Pair(true, "")
        }
    }

    private fun validateWalletCardSpent(spent: Double): Pair<Boolean, String> {
        return if(spent < 0) {
            Pair(false, "Valor inválido.")
        } else {
            Pair(true, "")
        }
    }
}