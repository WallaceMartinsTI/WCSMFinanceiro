package com.wcsm.wcsmfinanceiro.domain.usecase.bills

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.data.model.BillType
import com.wcsm.wcsmfinanceiro.data.model.PaymentType
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
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
class SaveBillUseCaseTest {

    @Mock
    private lateinit var billsRepository: BillsRepository

    private lateinit var saveBillUseCase: SaveBillUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        saveBillUseCase = SaveBillUseCase(billsRepository)
    }

    @Test
    fun saveBillUseCase_saveBillUseCaseWithSuccess_shouldEmitSuccessResponse() = runTest {
        // GIVEN: A bill to be saved
        val bill = Bill(
            billId = 1,
            billType = BillType.INCOME,
            origin = "Origem Teste",
            title = "Conta Teste",
            value = 2578.92,
            description = "Descrição Teste",
            date = 1737504000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        Mockito.`when`(billsRepository.saveBill(bill)).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Success(bill.billId))
            }
        )

        // WHEN: Trying to save the bill
        saveBillUseCase(bill).test {
            // THEN: Use case should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response
            assertThat((awaitItem() as Response.Success).data).isEqualTo(bill.billId)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveBillUseCase_saveBillUseCaseNoRowsAffected_shouldEmitErrorResponse() = runTest {
        // GIVEN: A bill to be saved
        val bill = Bill(
            billId = 1,
            billType = BillType.INCOME,
            origin = "Origem Teste",
            title = "Conta Teste",
            value = 2578.92,
            description = "Descrição Teste",
            date = 1737504000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )
        val errorMessage = "Erro ao salvar conta."

        Mockito.`when`(billsRepository.saveBill(bill)).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Error(errorMessage))
            }
        )

        // WHEN: Trying to save the bill
        saveBillUseCase(bill).test {
            // THEN: Use case should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo(errorMessage)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveBillUseCase_saveBillUseCaseInvalidValue_shouldEmitErrorResponse() = runTest {
        // GIVEN: A bill to be saved
        val bill = Bill(
            billId = 1,
            billType = BillType.INCOME,
            origin = "Origem Teste",
            title = "Conta Teste",
            value = 99999999.99,
            description = "Descrição Teste",
            date = 1737504000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        // WHEN: Trying to save the bill
        saveBillUseCase(bill).test {
            // THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo("Valor muito alto (max. R$9.999.999,99).")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }
}