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
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetBillsByDateUseCaseTest {

    @Mock
    private lateinit var billsRepository: BillsRepository

    private lateinit var getBillsByDateUseCase: GetBillsByDateUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        getBillsByDateUseCase = GetBillsByDateUseCase(billsRepository)
    }

    @Test
    fun getBillsByDateUseCase_getBillsByDateUseCaseWithSuccess_shouldEmitSuccessResponse() = runTest {
        val expectedBillsList = listOf(
            Bill(
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
            ),
            Bill(
                billId = 2,
                billType = BillType.EXPENSE,
                origin = "Origem Teste2",
                title = "Conta Teste2",
                value = 2578.92,
                description = "Descrição Teste2",
                date = 1737504000000,
                paymentType = PaymentType.CARD,
                paid = true,
                dueDate = 1737804000000,
                expired = false,
                category = "Lazer",
                tags = listOf("lazer", "casa", "construção")
            )
        )

        Mockito.`when`(billsRepository.getBillsByDate(anyLong(), anyLong())).thenReturn(
            flow {
                emit(DatabaseResponse.Loading)
                emit(DatabaseResponse.Success(expectedBillsList))
            }
        )

        // GIVEN & WHEN: A request for the bills by date
        getBillsByDateUseCase(1, 2).test {
            // THEN: Use case should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(DatabaseResponse.Loading::class.java)

            // AND THEN: It should emit a success response
            assertThat((awaitItem() as DatabaseResponse.Success).data).isEqualTo(expectedBillsList)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getBillsByDateUseCase_getBillsByDateUseCaseNoBillsForTheInformedDate_shouldEmitSuccessResponseWithAnEmptyList() = runTest {
        Mockito.`when`(billsRepository.getBillsByDate(anyLong(), anyLong())).thenReturn(
            flow {
                emit(DatabaseResponse.Loading)
                emit(DatabaseResponse.Success(emptyList()))
            }
        )

        // GIVEN & WHEN: A request for the bills by date
        getBillsByDateUseCase(1, 2).test {
            // THEN: Use case should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(DatabaseResponse.Loading::class.java)

            // AND THEN: It should emit a success response with an empty list
            assertThat((awaitItem() as DatabaseResponse.Success).data).isEmpty()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }
}