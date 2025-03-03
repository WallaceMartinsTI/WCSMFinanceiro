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
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetBillsUseCaseTest {

    @Mock
    private lateinit var billsRepository: BillsRepository

    private lateinit var getBillsUseCase: GetBillsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        getBillsUseCase = GetBillsUseCase(billsRepository)
    }

    @Test
    fun getBillsUseCase_getBillsUseCaseWithSuccess_shouldEmitSuccessResponse() = runTest {
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

        Mockito.`when`(billsRepository.getBills()).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Success(expectedBillsList))
            }
        )

        // WHEN: Trying to delete the bill
        getBillsUseCase().test {
            // THEN: Use case should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response
            assertThat((awaitItem() as Response.Success).data).isEqualTo(expectedBillsList)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getBillsUseCase_getBillsUseCaseNoBillsForTheInformedText_shouldEmitSuccessResponseWithAnEmptyList() = runTest {
        Mockito.`when`(billsRepository.getBills()).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Success(emptyList()))
            }
        )

        // WHEN: Trying to delete the bill
        getBillsUseCase().test {
            // THEN: Use case should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            // AND THEN: It should emit a success response
            assertThat((awaitItem() as Response.Success).data).isEmpty()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }
}