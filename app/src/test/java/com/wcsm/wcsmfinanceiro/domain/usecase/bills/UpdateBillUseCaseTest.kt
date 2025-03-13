package com.wcsm.wcsmfinanceiro.domain.usecase.bills

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.data.local.entity.Bill
import com.wcsm.wcsmfinanceiro.data.local.model.BillType
import com.wcsm.wcsmfinanceiro.data.local.model.PaymentType
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class UpdateBillUseCaseTest {

    @Mock
    private lateinit var billsRepository: BillsRepository

    private lateinit var updateBillUseCase: UpdateBillUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        updateBillUseCase = UpdateBillUseCase(billsRepository)
    }

    @Test
    fun updateBillUseCase_updateBillUseCaseWithSuccess_shouldEmitSuccessResponse() = runTest {
        // GIVEN: A bill to be updated
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

        Mockito.`when`(billsRepository.updateBill(bill)).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Success(1))
            }
        )

        // WHEN: Trying to update the bill
        updateBillUseCase(bill).test {
            // THEN: Use case should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response
            val rowsAffected = (awaitItem() as Response.Success).data
            assertThat(rowsAffected).isEqualTo(1)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateBillUseCase_updateBillUseCaseNoRowsAffected_shouldEmitErrorResponse() = runTest {
        // GIVEN: A bill to be updated
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
        val errorMessage = "Erro ao atualizar conta."

        Mockito.`when`(billsRepository.updateBill(bill)).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Error(errorMessage))
            }
        )

        // WHEN: Trying to update the bill
        updateBillUseCase(bill).test {
            // THEN: Use case should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo(errorMessage)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateBillUseCase_updateBillUseCaseInvalidValue_shouldEmitErrorResponse() = runTest {
        // GIVEN: A bill to be updated
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

        // WHEN: Trying to update the bill
        updateBillUseCase(bill).test {
            // THEN: It should emit an error response
            assertThat((awaitItem() as Response.Error).message).isEqualTo("Valor muito alto (max. R$9.999.999,99).")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }
}