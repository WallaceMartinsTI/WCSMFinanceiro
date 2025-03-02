package com.wcsm.wcsmfinanceiro.domain.usecase.bills

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.data.model.BillType
import com.wcsm.wcsmfinanceiro.data.model.PaymentType
import com.wcsm.wcsmfinanceiro.domain.model.DatabaseResponse
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
class DeleteBillUseCaseTest {

    @Mock
    private lateinit var billsRepository: BillsRepository

    private lateinit var deleteBillUseCase: DeleteBillUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        deleteBillUseCase = DeleteBillUseCase(billsRepository)
    }

    @Test
    fun deleteBillUseCase_deleteBillUseCaseWithSuccess_shouldEmitSuccessResponse() = runTest {
        // GIVEN: A bill to be deleted
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

        Mockito.`when`(billsRepository.deleteBill(bill)).thenReturn(
            flow {
                emit(DatabaseResponse.Loading)
                emit(DatabaseResponse.Success(1))
            }
        )

        // WHEN: Trying to delete the bill
        deleteBillUseCase(bill).test {
            // THEN: Use case should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(DatabaseResponse.Loading::class.java)

            // AND THEN: It should emit a success response
            val rowsAffected = (awaitItem() as DatabaseResponse.Success).data
            assertThat(rowsAffected).isEqualTo(1)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteBillUseCase_deleteBillUseCaseNoRowsAffected_shouldEmitErrorResponse() = runTest {
        // GIVEN: A bill to be deleted
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
        val errorMessage = "Erro ao deletar conta."

        Mockito.`when`(billsRepository.deleteBill(bill)).thenReturn(
            flow {
                emit(DatabaseResponse.Loading)
                emit(DatabaseResponse.Error(errorMessage))
            }
        )

        // WHEN: Trying to delete the bill
        deleteBillUseCase(bill).test {
            // THEN: Use case should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(DatabaseResponse.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as DatabaseResponse.Error).message).isEqualTo(errorMessage)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }
}