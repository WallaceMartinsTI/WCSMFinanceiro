package com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.data.local.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.local.entity.WalletCard
import com.wcsm.wcsmfinanceiro.data.local.entity.relation.WalletWithCards
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.DeleteWalletCardUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.DeleteWalletUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.GetWalletWithCardsUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.SaveWalletCardUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.SaveWalletUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.UpdateWalletCardUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.UpdateWalletUseCase
import com.wcsm.wcsmfinanceiro.presentation.model.UiState
import com.wcsm.wcsmfinanceiro.presentation.model.wallet.WalletCardState
import com.wcsm.wcsmfinanceiro.presentation.model.wallet.WalletOperationType
import com.wcsm.wcsmfinanceiro.presentation.model.wallet.WalletState
import com.wcsm.wcsmfinanceiro.util.toWallet
import com.wcsm.wcsmfinanceiro.util.toWalletCard
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WalletViewModelTest {

    @Mock
    private lateinit var getWalletWithCardsUseCase: GetWalletWithCardsUseCase
    @Mock
    private lateinit var saveWalletUseCase: SaveWalletUseCase
    @Mock
    private lateinit var updateWalletUseCase: UpdateWalletUseCase
    @Mock
    private lateinit var deleteWalletUseCase: DeleteWalletUseCase
    @Mock
    private lateinit var saveWalletCardUseCase: SaveWalletCardUseCase
    @Mock
    private lateinit var updateWalletCardUseCase: UpdateWalletCardUseCase
    @Mock
    private lateinit var deleteWalletCardUseCase: DeleteWalletCardUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun updateWalletState_updateWalletState_shouldUpdateCorrectly() = runTest {
        val walletViewModel = WalletViewModel(
            getWalletWithCardsUseCase, saveWalletUseCase, updateWalletUseCase, deleteWalletUseCase,
            saveWalletCardUseCase, updateWalletCardUseCase, deleteWalletCardUseCase
        )

        // GIVEN: A new walletState to be updated
        val walletState = WalletState()

        walletViewModel.walletStateFlow.test {
            // At first walletStateFlow state should start with an empty WalletState
            assertThat(awaitItem()).isEqualTo(walletState)

            // WHEN: Passed a new wallet state to be updated
            walletViewModel.updateWalletState(walletState.copy(title = "Título Atualizado"))

            // THEN: Wallet state should match with updated WalletState
            assertThat(awaitItem().title).isEqualTo("Título Atualizado")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateWalletCardState_updateWalletCardState_shouldUpdateCorrectly() = runTest {
        val walletViewModel = WalletViewModel(
            getWalletWithCardsUseCase, saveWalletUseCase, updateWalletUseCase, deleteWalletUseCase,
            saveWalletCardUseCase, updateWalletCardUseCase, deleteWalletCardUseCase
        )

        // GIVEN: A new walletState to be updated
        val walletCardState = WalletCardState()

        walletViewModel.walletCardStateFlow.test {
            // At first walletStateFlow state should start with an empty WalletState
            assertThat(awaitItem()).isEqualTo(walletCardState)

            // WHEN: Passed a new wallet state to be updated
            walletViewModel.updateWalletCardState(walletCardState.copy(title = "Título Atualizado"))

            // THEN: Wallet state should match with updated WalletState
            assertThat(awaitItem().title).isEqualTo("Título Atualizado")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateUiState_updateUiState_shouldUpdateUiStateCorrectly() = runTest {
        val walletViewModel = WalletViewModel(
            getWalletWithCardsUseCase, saveWalletUseCase, updateWalletUseCase, deleteWalletUseCase,
            saveWalletCardUseCase, updateWalletCardUseCase, deleteWalletCardUseCase
        )

        // GIVEN: A new uiState to be updated
        val uiState = UiState<WalletOperationType>()

        walletViewModel.uiState.test {
            // At first uiState should start with an empty UiState
            assertThat(awaitItem()).isEqualTo(uiState)

            // WHEN: Passed a new uiState to be updated
            walletViewModel.updateUiState(uiState.copy(isLoading = true))

            // THEN: uiState should match with updated uiState
            assertThat(awaitItem().isLoading).isTrue()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun resetWalletState_resetWalletState_shouldMatchWithAnEmptyWalletState() = runTest {
        val walletViewModel = WalletViewModel(
            getWalletWithCardsUseCase, saveWalletUseCase, updateWalletUseCase, deleteWalletUseCase,
            saveWalletCardUseCase, updateWalletCardUseCase, deleteWalletCardUseCase
        )

        // GIVEN: A reset wallet state
        val walletState = WalletState()

        walletViewModel.walletStateFlow.test {
            // At first walletStateFlow state should start with an empty WalletState
            assertThat(awaitItem()).isEqualTo(walletState)

            // Update a wallet state to be reset
            walletViewModel.updateWalletState(walletState.copy(title = "WalletState Atualizado"))

            // Check if wallet state was updated
            assertThat(awaitItem().title).isEqualTo("WalletState Atualizado")

            // WHEN: Reset wallet state
            walletViewModel.resetWalletState()

            // THEN: Wallet state should match with a reset walletState
            assertThat(awaitItem()).isEqualTo(walletState)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun resetWalletCardState_resetWalletCardState_shouldMatchWithAnEmptyWalletState() = runTest {
        val walletViewModel = WalletViewModel(
            getWalletWithCardsUseCase, saveWalletUseCase, updateWalletUseCase, deleteWalletUseCase,
            saveWalletCardUseCase, updateWalletCardUseCase, deleteWalletCardUseCase
        )

        // GIVEN: A reset walletCard state
        val walletCardState = WalletCardState()

        walletViewModel.walletCardStateFlow.test {
            // At first walletCardStateFlow state should start with an empty WalletCardState
            assertThat(awaitItem()).isEqualTo(walletCardState)

            // Update a walletCard state to be reset
            walletViewModel.updateWalletCardState(walletCardState.copy(title = "WalletState Atualizado"))

            // Check if walletCard state was updated
            assertThat(awaitItem().title).isEqualTo("WalletState Atualizado")

            // WHEN: Reset walletCard state
            walletViewModel.resetWalletCardState()

            // THEN: WalletCard state should match with a reset walletCardState
            assertThat(awaitItem()).isEqualTo(walletCardState)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun resetUiState_resetUiState_shouldMatchWithAnEmptyUiState() = runTest {
        val walletViewModel = WalletViewModel(
            getWalletWithCardsUseCase, saveWalletUseCase, updateWalletUseCase, deleteWalletUseCase,
            saveWalletCardUseCase, updateWalletCardUseCase, deleteWalletCardUseCase
        )

        // GIVEN: A reset ui state
        val uiState = UiState<WalletOperationType>()

        walletViewModel.uiState.test {
            // At first uiState state should start with an empty UiState
            assertThat(awaitItem()).isEqualTo(uiState)

            // Update uiState to be reset
            walletViewModel.updateUiState(uiState.copy(error = "Erro simulado"))

            assertThat(awaitItem().error).isEqualTo("Erro simulado")

            // WHEN: Reset uiState
            walletViewModel.resetUiState()

            // THEN: uiState should match with a reset uiState
            assertThat(awaitItem()).isEqualTo(uiState)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getWalletWithCards_getWalletWithCards_shouldFillWalletWithCardsList() = runTest {
        val walletWithCards = listOf(
            WalletWithCards(
                wallet = Wallet(
                    walletId = 1L,
                    title = "Carteira Principal",
                    balance = 5000.00
                ),
                walletCards = listOf(
                    WalletCard(
                        walletCardId = 1L,
                        walletId = 1L,
                        title = "Cartão Visa Platinum",
                        limit = 10000.00,
                        spent = 2500.00,
                        available = 7500.00,
                        blocked = false
                    ),
                    WalletCard(
                        walletCardId = 2L,
                        walletId = 1L,
                        title = "Cartão Mastercard Gold",
                        limit = 5000.00,
                        spent = 1000.00,
                        available = 4000.00,
                        blocked = false
                    )
                )
            ),
            WalletWithCards(
                wallet = Wallet(
                    walletId = 2L,
                    title = "Carteira Viagens",
                    balance = 2000.00
                ),
                walletCards = listOf(
                    WalletCard(
                        walletCardId = 3L,
                        walletId = 2L,
                        title = "Cartão American Express",
                        limit = 15000.00,
                        spent = 5000.00,
                        available = 10000.00,
                        blocked = false
                    ),
                    WalletCard(
                        walletCardId = 4L,
                        walletId = 2L,
                        title = "Cartão Elo Nanquim",
                        limit = 8000.00,
                        spent = 2000.00,
                        available = 6000.00,
                        blocked = false
                    )
                )
            ),
            WalletWithCards(
                wallet = Wallet(
                    walletId = 3L,
                    title = "Carteira Emergências",
                    balance = 10000.00
                ),
                walletCards = listOf(
                    WalletCard(
                        walletCardId = 5L,
                        walletId = 3L,
                        title = "Cartão Santander Free",
                        limit = 3000.00,
                        spent = 500.00,
                        available = 2500.00,
                        blocked = false
                    ),
                    WalletCard(
                        walletCardId = 6L,
                        walletId = 3L,
                        title = "Cartão Nubank",
                        limit = 7000.00,
                        spent = 3500.00,
                        available = 3500.00,
                        blocked = false
                    )
                )
            )
        )

        val walletViewModel = WalletViewModel(
            getWalletWithCardsUseCase, saveWalletUseCase, updateWalletUseCase, deleteWalletUseCase,
            saveWalletCardUseCase, updateWalletCardUseCase, deleteWalletCardUseCase
        )

        Mockito.`when`(getWalletWithCardsUseCase()).thenReturn(
            flow { emit(Response.Success(walletWithCards)) }
        )

        walletViewModel.walletsWithCards.test {
            assertThat(awaitItem()).isNull()

            // GIVEN & WHEN: List of walletWithCards is requested
            walletViewModel.getWalletWithCards()

            // THEN: walletWithCards list should be filled with requested walletWithCards and reversed
            assertThat(awaitItem()).isEqualTo(walletWithCards.reversed())

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveWallet_saveWalletToUseCase_shouldSaveWalletAndFillUiStateWithSuccess() = runTest {
        // GIVEN: A wallet to be saved
        val walletToBeSaved = WalletState(
            walletId = 1L,
            title = "Carteira Principal",
            balance = 5000.00
        )

        val walletViewModel = WalletViewModel(
            getWalletWithCardsUseCase, saveWalletUseCase, updateWalletUseCase, deleteWalletUseCase,
            saveWalletCardUseCase, updateWalletCardUseCase, deleteWalletCardUseCase
        )

        Mockito.`when`(saveWalletUseCase(walletToBeSaved.toWallet())).thenReturn(
            flow { emit(Response.Success(1L)) }
        )

        Mockito.`when`(getWalletWithCardsUseCase()).thenReturn(
            flow { emit(Response.Success(emptyList())) }
        )

        walletViewModel.updateWalletState(walletToBeSaved)

        walletViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(UiState<WalletOperationType>())

            // WHEN: Save the wallet
            walletViewModel.saveWallet(walletToBeSaved)

            assertThat(awaitItem().operationType).isInstanceOf(WalletOperationType.Save::class.java)

            // THEN: Should emit success
            assertThat(awaitItem().success).isTrue()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateWallet_updateWalletToUseCase_shouldUpdateWalletAndFillUiStateWithSuccess() = runTest {
        // GIVEN: A wallet to be updated
        val walletToBeUpdated = WalletState(
            walletId = 1L,
            title = "Carteira Principal",
            balance = 5000.00
        )

        val walletViewModel = WalletViewModel(
            getWalletWithCardsUseCase, saveWalletUseCase, updateWalletUseCase, deleteWalletUseCase,
            saveWalletCardUseCase, updateWalletCardUseCase, deleteWalletCardUseCase
        )

        Mockito.`when`(updateWalletUseCase(walletToBeUpdated.toWallet())).thenReturn(
            flow { emit(Response.Success(1)) }
        )

        Mockito.`when`(getWalletWithCardsUseCase()).thenReturn(
            flow { emit(Response.Success(emptyList())) }
        )

        walletViewModel.updateWalletState(walletToBeUpdated)

        walletViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(UiState<WalletOperationType>())

            // WHEN: Update the wallet
            walletViewModel.updateWallet(walletToBeUpdated)

            assertThat(awaitItem().operationType).isInstanceOf(WalletOperationType.Update::class.java)

            // THEN: Should emit success
            assertThat(awaitItem().success).isTrue()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteWallet_deleteWalletToUseCase_shouldDeleteWalletAndFillUiStateWithSuccess() = runTest {
        // GIVEN: A wallet to be deleted
        val walletToBeDeleted = WalletState(
            walletId = 1L,
            title = "Carteira Principal",
            balance = 5000.00
        )

        val walletViewModel = WalletViewModel(
            getWalletWithCardsUseCase, saveWalletUseCase, updateWalletUseCase, deleteWalletUseCase,
            saveWalletCardUseCase, updateWalletCardUseCase, deleteWalletCardUseCase
        )

        Mockito.`when`(deleteWalletUseCase(walletToBeDeleted.toWallet())).thenReturn(
            flow { emit(Response.Success(1)) }
        )

        Mockito.`when`(getWalletWithCardsUseCase()).thenReturn(
            flow { emit(Response.Success(emptyList())) }
        )

        walletViewModel.updateWalletState(walletToBeDeleted)

        walletViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(UiState<WalletOperationType>())

            // WHEN: Delete the wallet
            walletViewModel.deleteWallet(walletToBeDeleted)

            assertThat(awaitItem().operationType).isInstanceOf(WalletOperationType.Delete::class.java)

            // THEN: Should emit success
            assertThat(awaitItem().success).isTrue()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveCardWallet_saveWalletCardToUseCase_shouldSaveWalletCardAndFillUiStateWithSuccess() = runTest {
        // WalletWithCards, It's necessary to have a walletCard
        val walletWithCardsList = listOf(
            WalletWithCards(
                wallet = Wallet(
                    walletId = 5L,
                    title = "Carteira Principal",
                    balance = 5000.00
                ),
                walletCards = emptyList()
            )
        )

        // GIVEN: A walletCard to be saved
        val walletCardToBeSaved = WalletCardState(
            walletId = 5L,
            walletCardId = 1L,
            title = "Carteira Principal",
            limit = 7000.00,
            available = 5000.00,
            spent = 2000.00
        )

        val walletViewModel = WalletViewModel(
            getWalletWithCardsUseCase, saveWalletUseCase, updateWalletUseCase, deleteWalletUseCase,
            saveWalletCardUseCase, updateWalletCardUseCase, deleteWalletCardUseCase
        )

        Mockito.`when`(saveWalletCardUseCase(walletCardToBeSaved.toWalletCard())).thenReturn(
            flow { emit(Response.Success(1L)) }
        )


        Mockito.`when`(getWalletWithCardsUseCase()).thenReturn(
            flow { emit(Response.Success(walletWithCardsList)) }
        )

        walletViewModel.updateWalletCardState(walletCardToBeSaved)

        walletViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(UiState<WalletOperationType>())

            walletViewModel.getWalletWithCards()
            awaitItem()

            walletViewModel.resetUiState()
            awaitItem()

            // WHEN: Save the walletCard
            walletViewModel.saveWalletCard(walletCardToBeSaved)

            assertThat(awaitItem().operationType).isInstanceOf(WalletOperationType.Save::class.java)

            // THEN: Should emit success
            assertThat(awaitItem().success).isTrue()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateCardWallet_updateWalletCardToUseCase_shouldUpdateWalletCardAndFillUiStateWithSuccess() = runTest {
        // WalletWithCards, It's necessary to have a walletCard
        val walletWithCardsList = listOf(
            WalletWithCards(
                wallet = Wallet(
                    walletId = 5L,
                    title = "Carteira Principal",
                    balance = 5000.00
                ),
                walletCards = emptyList()
            )
        )

        // GIVEN: A walletCard to be updated
        val walletCardToBeUpdated = WalletCardState(
            walletId = 5L,
            walletCardId = 1L,
            title = "Carteira Principal",
            limit = 7000.00,
            available = 5000.00,
            spent = 2000.00
        )

        val walletViewModel = WalletViewModel(
            getWalletWithCardsUseCase, saveWalletUseCase, updateWalletUseCase, deleteWalletUseCase,
            saveWalletCardUseCase, updateWalletCardUseCase, deleteWalletCardUseCase
        )

        Mockito.`when`(updateWalletCardUseCase(walletCardToBeUpdated.toWalletCard())).thenReturn(
            flow { emit(Response.Success(1)) }
        )


        Mockito.`when`(getWalletWithCardsUseCase()).thenReturn(
            flow { emit(Response.Success(walletWithCardsList)) }
        )

        walletViewModel.updateWalletCardState(walletCardToBeUpdated)

        walletViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(UiState<WalletOperationType>())

            walletViewModel.getWalletWithCards()
            awaitItem()

            walletViewModel.resetUiState()
            awaitItem()

            // WHEN: Update the walletCard
            walletViewModel.updateWalletCard(walletCardToBeUpdated)

            assertThat(awaitItem().operationType).isInstanceOf(WalletOperationType.Update::class.java)

            // THEN: Should emit success
            assertThat(awaitItem().success).isTrue()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteCardWallet_deleteWalletCardToUseCase_shouldUpdateWalletCardAndFillUiStateWithSuccess() = runTest {
        // WalletWithCards, It's necessary to have a walletCard
        val walletWithCardsList = listOf(
            WalletWithCards(
                wallet = Wallet(
                    walletId = 5L,
                    title = "Carteira Principal",
                    balance = 5000.00
                ),
                walletCards = emptyList()
            )
        )

        // GIVEN: A walletCard to be deleted
        val walletCardToBeDeleted = WalletCardState(
            walletId = 5L,
            walletCardId = 1L,
            title = "Carteira Principal",
            limit = 7000.00,
            available = 5000.00,
            spent = 2000.00
        )

        val walletViewModel = WalletViewModel(
            getWalletWithCardsUseCase, saveWalletUseCase, updateWalletUseCase, deleteWalletUseCase,
            saveWalletCardUseCase, updateWalletCardUseCase, deleteWalletCardUseCase
        )

        Mockito.`when`(deleteWalletCardUseCase(walletCardToBeDeleted.toWalletCard())).thenReturn(
            flow { emit(Response.Success(1)) }
        )


        Mockito.`when`(getWalletWithCardsUseCase()).thenReturn(
            flow { emit(Response.Success(walletWithCardsList)) }
        )

        walletViewModel.updateWalletCardState(walletCardToBeDeleted)

        walletViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(UiState<WalletOperationType>())

            walletViewModel.getWalletWithCards()
            awaitItem()

            walletViewModel.resetUiState()
            awaitItem()

            // WHEN: Delete the walletCard
            walletViewModel.deleteWalletCard(walletCardToBeDeleted)

            assertThat(awaitItem().operationType).isInstanceOf(WalletOperationType.Delete::class.java)

            // THEN: Should emit success
            assertThat(awaitItem().success).isTrue()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }
}