package com.wcsm.wcsmfinanceiro.data.repository

import android.database.sqlite.SQLiteConstraintException
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.data.database.dao.WalletDao
import com.wcsm.wcsmfinanceiro.data.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.entity.WalletCard
import com.wcsm.wcsmfinanceiro.data.entity.relation.WalletWithCards
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.WalletRepository
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
class WalletRepositoryImplTest {

    @Mock
    private lateinit var walletDao: WalletDao

    private lateinit var walletRepository: WalletRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        walletRepository = WalletRepositoryImpl(walletDao)
    }

    @Test
    fun saveWallet_saveWalletWithSuccess_shouldEmitSuccessResponse() = runTest {
        // GIVEN: A wallet to be saved
        val wallet = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 1000.0
        )

        Mockito.`when`(walletDao.saveWallet(wallet)).thenReturn(1)

        // WHEN: Trying to save the wallet
        walletRepository.saveWallet(wallet).test {
            // THEN: Repository should Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response
            assertThat((awaitItem() as Response.Success).data).isEqualTo(wallet.walletId)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveWallet_saveWalletNoRowsAffected_shouldEmitErrorResponse() = runTest {
        // GIVEN: A wallet to be saved
        val wallet = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 1000.0
        )

        Mockito.`when`(walletDao.saveWallet(wallet)).thenReturn(0)

        // WHEN: Trying to save the wallet
        walletRepository.saveWallet(wallet).test {
            // THEN: Repository should Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo("Erro ao salvar carteira.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveWallet_saveWalletTwice_shouldEmitErrorResponse() = runTest {
        // GIVEN: A wallet to be saved
        val wallet = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 1000.0
        )

        Mockito.`when`(walletDao.saveWallet(wallet)).thenThrow(SQLiteConstraintException())

        // WHEN: Trying to save the wallet
        walletRepository.saveWallet(wallet).test {
            // THEN: Repository should Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo("Erro desconhecido ao salvar carteira, informe o administrador.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateWallet_updateWalletWithSuccess_shouldEmitSuccessResponse() = runTest {
        // GIVEN: A wallet to be saved
        val wallet = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 1000.0
        )

        Mockito.`when`(walletDao.updateWallet(wallet)).thenReturn(1)

        // WHEN: Trying to update the wallet
        walletRepository.updateWallet(wallet).test {
            // THEN: Repository should Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response
            val rowsAffected = (awaitItem() as Response.Success).data
            assertThat(rowsAffected).isEqualTo(1)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateWallet_updateWalletNoRowsAffected_shouldEmitErrorResponse() = runTest {
        // GIVEN: A wallet to be saved
        val wallet = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 1000.0
        )

        Mockito.`when`(walletDao.updateWallet(wallet)).thenReturn(0)

        // WHEN: Trying to update the wallet
        walletRepository.updateWallet(wallet).test {
            // THEN: Repository should Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo("Erro ao atualizar carteira.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteWallet_deleteWalletWithSuccess_shouldEmitSuccessResponse() = runTest {
        // GIVEN: A wallet to be deleted
        val wallet = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 1000.0
        )

        Mockito.`when`(walletDao.deleteWallet(wallet)).thenReturn(1)

        // WHEN: Trying to delete the wallet
        walletRepository.deleteWallet(wallet).test {
            // THEN: Repository should Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response
            val rowsAffected = (awaitItem() as Response.Success).data
            assertThat(rowsAffected).isEqualTo(1)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteWallet_deleteWalletNoRowsAffected_shouldEmitErrorResponse() = runTest {
        // GIVEN: A wallet to be deleted
        val wallet = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 1000.0
        )

        Mockito.`when`(walletDao.deleteWallet(wallet)).thenReturn(0)

        // WHEN: Trying to delete the wallet
        walletRepository.deleteWallet(wallet).test {
            // THEN: Repository should Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo("Erro ao deletar carteira.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getWalletWithCards_getWalletWithCardsWithSuccess_shouldEmitSuccessResponse() = runTest {
        val expectedWalletWithCards = listOf(
            WalletWithCards(
                wallet = Wallet(
                    walletId = 1,
                    title = "Nubank",
                    balance = 1000.0
                ),
                walletCards = listOf(
                    WalletCard(
                        walletId = 1,
                        walletCardId = 1,
                        title = "Credit Card",
                        limit = 2000.0,
                        spent = 1000.0,
                        available = 1000.0,
                        blocked = false
                    ),
                    WalletCard(
                        walletId = 1,
                        walletCardId = 2,
                        title = "Credit Card",
                        limit = 2000.0,
                        spent = 1000.0,
                        available = 1000.0,
                        blocked = false
                    )
                )
            ),
            WalletWithCards(
                wallet = Wallet(
                    walletId = 2,
                    title = "Inter",
                    balance = 1000.0
                ),
                walletCards = listOf(
                    WalletCard(
                        walletId = 2,
                        walletCardId = 3,
                        title = "Credit Card",
                        limit = 2000.0,
                        spent = 1000.0,
                        available = 1000.0,
                        blocked = false
                    ),
                    WalletCard(
                        walletId = 2,
                        walletCardId = 4,
                        title = "Credit Card",
                        limit = 2000.0,
                        spent = 1000.0,
                        available = 1000.0,
                        blocked = false
                    )
                )
            )
        )

        Mockito.`when`(walletDao.selectAllWalletWithCards()).thenReturn(
            flow {
                emit(expectedWalletWithCards)
            }
        )

        // GIVEN & WHEN: A request to select all wallet with cards
        walletRepository.getWalletWithCards().test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response with the wallet with cards list
            assertThat((awaitItem() as Response.Success).data).isEqualTo(expectedWalletWithCards)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getWalletWithCards_getWalletWithCardsWithSuccess_shouldEmitSuccessResponseAndEmptyList() = runTest {
        Mockito.`when`(walletDao.selectAllWalletWithCards()).thenReturn(
            flow {
                emit(emptyList())
            }
        )

        // GIVEN & WHEN: A request to select all wallet with cards
        walletRepository.getWalletWithCards().test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response with an empty list
            assertThat((awaitItem() as Response.Success).data).isEmpty()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getWalletWithCards_getWalletWithCardsWithDatabaseError_shouldEmitErrorResponse() = runTest {
        Mockito.`when`(walletDao.selectAllWalletWithCards()).thenThrow(RuntimeException())

        // GIVEN & WHEN: A request to select all wallet with cards
        walletRepository.getWalletWithCards().test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo("Erro desconhecido ao buscar carteiras, informe o administrador.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

}