package com.wcsm.wcsmfinanceiro.data.repository

import android.database.sqlite.SQLiteConstraintException
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.data.database.dao.WalletCardDao
import com.wcsm.wcsmfinanceiro.data.entity.WalletCard
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.WalletCardRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WalletCardRepositoryImplTest {

    @Mock
    private lateinit var walletCardDao: WalletCardDao

    private lateinit var walletCardRepository: WalletCardRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        walletCardRepository = WalletCardRepositoryImpl(walletCardDao)
    }

    @Test
    fun saveWalletCard_saveWalletCardWithSuccess_shouldEmitSuccessResponse() = runTest {
        // GIVEN: A wallet card to be saved
        val walletCard = WalletCard(
            walletId = 1,
            walletCardId = 1,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        Mockito.`when`(walletCardDao.saveWalletCard(walletCard)).thenReturn(walletCard.walletCardId)

        // WHEN: Trying to save the wallet card
        walletCardRepository.saveWalletCard(walletCard).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response
            assertThat((awaitItem() as Response.Success).data).isEqualTo(walletCard.walletCardId)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveWalletCard_saveWalletCardNoRowsAffected_shouldEmitErrorResponse() = runTest {
        // GIVEN: A wallet card to be saved
        val walletCard = WalletCard(
            walletId = 1,
            walletCardId = 1,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        Mockito.`when`(walletCardDao.saveWalletCard(walletCard)).thenReturn(0)

        // WHEN: Trying to save the wallet card
        walletCardRepository.saveWalletCard(walletCard).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo("Erro ao salvar cartão.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveWalletCard_saveWalletCardTwice_shouldEmitErrorResponse() = runTest {
        // GIVEN: A wallet card to be saved
        val walletCard = WalletCard(
            walletId = 1,
            walletCardId = 1,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        Mockito.`when`(walletCardDao.saveWalletCard(walletCard)).thenThrow(SQLiteConstraintException())

        // WHEN: Trying to save the wallet card
        walletCardRepository.saveWalletCard(walletCard).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo("Erro desconhecido ao salvar cartão, informe o administrador.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateWalletCard_updateWalletCardWithSuccess_shouldEmitSuccessResponse() = runTest {
        // GIVEN: A wallet card to be updated
        val walletCard = WalletCard(
            walletId = 1,
            walletCardId = 1,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        Mockito.`when`(walletCardDao.updateWalletCard(walletCard)).thenReturn(1)

        // WHEN: Trying to update the wallet card
        walletCardRepository.updateWalletCard(walletCard).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response
            val rowsAffected = (awaitItem() as Response.Success).data
            assertThat(rowsAffected).isEqualTo(1)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateWalletCard_updateWalletCardNoRowsAffected_shouldEmitErrorResponse() = runTest {
        // GIVEN: A wallet card to be updated
        val walletCard = WalletCard(
            walletId = 1,
            walletCardId = 1,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        Mockito.`when`(walletCardDao.updateWalletCard(walletCard)).thenReturn(0)

        // WHEN: Trying to update the wallet card
        walletCardRepository.updateWalletCard(walletCard).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo("Erro ao atualizar cartão.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteWalletCard_deleteWalletCardWithSuccess_shouldEmitSuccessResponse() = runTest {
        // GIVEN: A wallet card to be deleted
        val walletCard = WalletCard(
            walletId = 1,
            walletCardId = 1,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        Mockito.`when`(walletCardDao.deleteWalletCard(walletCard)).thenReturn(1)

        // WHEN: Trying to delete the wallet card
        walletCardRepository.deleteWalletCard(walletCard).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response
            assertThat((awaitItem() as Response.Success).data).isEqualTo(1)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteWalletCard_deleteWalletCardNoRowsAffected_shouldEmitErrorResponse() = runTest {
        // GIVEN: A wallet card to be deleted
        val walletCard = WalletCard(
            walletId = 1,
            walletCardId = 1,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        Mockito.`when`(walletCardDao.deleteWalletCard(walletCard)).thenReturn(0)

        // WHEN: Trying to delete the wallet card
        walletCardRepository.deleteWalletCard(walletCard).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo("Erro ao deletar cartão.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }
}