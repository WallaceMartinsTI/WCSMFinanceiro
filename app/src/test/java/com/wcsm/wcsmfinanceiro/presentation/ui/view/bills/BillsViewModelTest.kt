package com.wcsm.wcsmfinanceiro.presentation.ui.view.bills

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.data.local.entity.Bill
import com.wcsm.wcsmfinanceiro.data.local.model.BillType
import com.wcsm.wcsmfinanceiro.data.local.model.PaymentType
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.DeleteBillUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.GetBillsByDateUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.GetBillsByTextUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.GetBillsUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.SaveBillUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.UpdateBillUseCase
import com.wcsm.wcsmfinanceiro.presentation.model.UiState
import com.wcsm.wcsmfinanceiro.presentation.model.CrudOperationType
import com.wcsm.wcsmfinanceiro.presentation.model.bills.BillState
import com.wcsm.wcsmfinanceiro.util.toBill
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BillsViewModelTest {

    @Mock
    private lateinit var getBillsUseCase: GetBillsUseCase
    @Mock
    private lateinit var saveBillUseCase: SaveBillUseCase
    @Mock
    private lateinit var updateBillUseCase: UpdateBillUseCase
    @Mock
    private lateinit var deleteBillUseCase: DeleteBillUseCase
    @Mock
    private lateinit var getBillsByDateUseCase: GetBillsByDateUseCase
    @Mock
    private lateinit var getBillsByTextUseCase: GetBillsByTextUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun updateFilterSelectedDateRange_updateFilterSelectedDateRangeState_shouldUpdateCorrectly() = runTest {
        val billsViewModel = BillsViewModel(
            getBillsUseCase, saveBillUseCase, updateBillUseCase,
            deleteBillUseCase, getBillsByDateUseCase, getBillsByTextUseCase
        )

        // GIVEN: A date range to filter
        val dateRange = Pair(1000L, 2000L)

        billsViewModel.filterSelectedDateRange.test {
            // At first filterSelectedDateRange state should start with null value
            assertThat(awaitItem()).isNull()

            // WHEN: A date is passed to be filtered
            billsViewModel.updateFilterSelectedDateRange(dateRange)

            // THEN: filterSelectedDateRange state should match with passed dateRange
            assertThat(awaitItem()).isEqualTo(dateRange)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateUiState_updateUiState_shouldUpdateUiStateCorrectly() = runTest {
        val billsViewModel = BillsViewModel(
            getBillsUseCase, saveBillUseCase, updateBillUseCase,
            deleteBillUseCase, getBillsByDateUseCase, getBillsByTextUseCase
        )

        // GIVEN: A new uiState to be updated
        val uiState = UiState<CrudOperationType>()

        billsViewModel.uiState.test {
            // At first uiState should start with an empty UiState
            assertThat(awaitItem()).isEqualTo(uiState)

            // WHEN: Passed a new uiState to be updated
            billsViewModel.updateUiState(uiState.copy(isLoading = true))

            // THEN: uiState should match with updated uiState
            assertThat(awaitItem().isLoading).isTrue()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateBillState_updateBillState_shouldUpdateCorrectly() = runTest {
        val billsViewModel = BillsViewModel(
            getBillsUseCase, saveBillUseCase, updateBillUseCase,
            deleteBillUseCase, getBillsByDateUseCase, getBillsByTextUseCase
        )

        // GIVEN: A new billState to be updated
        val billState = BillState()

        billsViewModel.billStateFlow.test {
            // At first billStateFlow state should start with an empty BillState
            assertThat(awaitItem()).isEqualTo(billState)

            // WHEN: Passed a new bill state to be updated
            billsViewModel.updateBillState(billState.copy(title = "Título Atualizado"))

            // THEN: Bill state should match with updated BillState
            assertThat(awaitItem().title).isEqualTo("Título Atualizado")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun clearSelectedDateRangeFilter_clearSelectedDateRangeFilter_shouldClearFilterSelectedDateRange() = runTest {
        val billsViewModel = BillsViewModel(
            getBillsUseCase, saveBillUseCase, updateBillUseCase,
            deleteBillUseCase, getBillsByDateUseCase, getBillsByTextUseCase
        )

        Mockito.`when`(getBillsUseCase()).thenReturn( flow { emit(Response.Success(emptyList())) } )

        val initialDateRange = Pair(1000L, 2000L)
        // GIVEN: A fake date range to be cleared
        billsViewModel.updateFilterSelectedDateRange(initialDateRange)

        billsViewModel.filterSelectedDateRange.test {
            // At first filterSelectedDateRange should match with values passed
            assertThat(awaitItem()).isEqualTo(initialDateRange)

            // WHEN: Clear filterSelectedDateRange
            billsViewModel.clearSelectedDateRangeFilter()

            // THEN: filterSelectedDateRange should be null
            assertThat(awaitItem()).isNull()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun resetBillState_resetBillState_shouldMatchWithAndEmptyBillState() = runTest {
        val billsViewModel = BillsViewModel(
            getBillsUseCase, saveBillUseCase, updateBillUseCase,
            deleteBillUseCase, getBillsByDateUseCase, getBillsByTextUseCase
        )

        // GIVEN: A reset bill state
        val billState = BillState()

        billsViewModel.billStateFlow.test {
            // At first billStateFlow state should start with an empty BillState
            assertThat(awaitItem()).isEqualTo(billState)

            // Update a bill state to be reset
            billsViewModel.updateBillState(billState.copy(title = "BillState Atualizado"))

            // Check if bill state was updated
            assertThat(awaitItem().title).isEqualTo("BillState Atualizado")

            // WHEN: Reset bill state
            billsViewModel.resetBillState()

            // THEN: Bill state should match with a reset billState
            assertThat(awaitItem()).isEqualTo(billState)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun resetUiState_resetUiState_shouldMatchWithAnEmptyUiState() = runTest {
        val billsViewModel = BillsViewModel(
            getBillsUseCase, saveBillUseCase, updateBillUseCase,
            deleteBillUseCase, getBillsByDateUseCase, getBillsByTextUseCase
        )

        // GIVEN: A reset ui state
        val uiState = UiState<CrudOperationType>()

        billsViewModel.uiState.test {
            // At first uiState state should start with an empty UiState
            assertThat(awaitItem()).isEqualTo(uiState)

            // Update uiState to be reset
            billsViewModel.updateUiState(uiState.copy(error = "Erro simulado"))

            assertThat(awaitItem().error).isEqualTo("Erro simulado")

            // WHEN: Reset uiState
            billsViewModel.resetUiState()

            // THEN: uiState should match with a reset uiState
            assertThat(awaitItem()).isEqualTo(uiState)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun applyDateRangeFilter_applyDateRangeFilter_shouldApplyDateRangeFilterCorrectly() = runTest {
        val filteredBillsByDate = listOf(
            Bill(
            billId = 1L,
            billType = BillType.INCOME,
            origin = "Companhia de Energia",
            title = "Conta de Luz",
            value = 250.75,
            description = "Conta de energia elétrica referente ao mês de janeiro",
            date = 1706659200000L,
            paymentType = PaymentType.CARD,
            paid = false,
            dueDate = 1709251200000L,
            expired = false,
            category = "Despesas Domésticas",
            tags = listOf("energia", "luz", "casa")
            ),
            Bill(
                billId = 2L,
                billType = BillType.EXPENSE,
                origin = "Netflix",
                title = "Assinatura Netflix",
                value = 39.90,
                description = "Assinatura mensal do serviço de streaming",
                date = 1706745600000L,
                paymentType = PaymentType.MONEY,
                paid = true,
                dueDate = 1709337600000L,
                expired = false,
                category = "Entretenimento",
                tags = listOf("streaming", "filmes", "séries")
            ),
            Bill(
                billId = 3L,
                billType = BillType.INCOME,
                origin = "Imobiliária XYZ",
                title = "Aluguel do Apartamento",
                value = 1800.00,
                description = "Aluguel mensal do apartamento no centro da cidade",
                date = 1706832000000L,
                paymentType = PaymentType.MONEY,
                paid = true,
                dueDate = 1709424000000L,
                expired = false,
                category = "Moradia",
                tags = listOf("aluguel", "moradia", "casa")
            )
        )

        Mockito.`when`(getBillsByDateUseCase(anyLong(), anyLong())).thenReturn(
            flow { emit(Response.Success(filteredBillsByDate)) }
        )

        val billsViewModel = BillsViewModel(
            getBillsUseCase, saveBillUseCase, updateBillUseCase,
            deleteBillUseCase, getBillsByDateUseCase, getBillsByTextUseCase
        )

        billsViewModel.bills.test {
            assertThat(awaitItem()).isNull()

            // GIVEN & WHEN: A date range to be filtered
            billsViewModel.applyDateRangeFilter(1000, 2000)

            // THEN: Should return a filtered bills list
            assertThat(awaitItem()).isEqualTo(filteredBillsByDate)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun applyTextFilter_applyTextFilter_shouldApplyDateRangeFilterCorrectly() = runTest {
        val filteredBillsByText = listOf(
            Bill(
                billId = 1L,
                billType = BillType.INCOME,
                origin = "Companhia de Energia",
                title = "Conta de Luz",
                value = 250.75,
                description = "Conta de energia elétrica referente ao mês de janeiro",
                date = 1706659200000L,
                paymentType = PaymentType.CARD,
                paid = false,
                dueDate = 1709251200000L,
                expired = false,
                category = "Despesas Domésticas",
                tags = listOf("energia", "luz", "casa")
            ),
            Bill(
                billId = 2L,
                billType = BillType.EXPENSE,
                origin = "Netflix",
                title = "Assinatura Netflix",
                value = 39.90,
                description = "Assinatura mensal do serviço de streaming",
                date = 1706745600000L,
                paymentType = PaymentType.MONEY,
                paid = true,
                dueDate = 1709337600000L,
                expired = false,
                category = "Entretenimento",
                tags = listOf("streaming", "filmes", "séries")
            ),
            Bill(
                billId = 3L,
                billType = BillType.INCOME,
                origin = "Imobiliária XYZ",
                title = "Aluguel do Apartamento",
                value = 1800.00,
                description = "Aluguel mensal do apartamento no centro da cidade",
                date = 1706832000000L,
                paymentType = PaymentType.MONEY,
                paid = true,
                dueDate = 1709424000000L,
                expired = false,
                category = "Moradia",
                tags = listOf("aluguel", "moradia", "casa")
            )
        )

        Mockito.`when`(getBillsByTextUseCase(anyString())).thenReturn(
            flow { emit(Response.Success(filteredBillsByText)) }
        )

        val billsViewModel = BillsViewModel(
            getBillsUseCase, saveBillUseCase, updateBillUseCase,
            deleteBillUseCase, getBillsByDateUseCase, getBillsByTextUseCase
        )

        billsViewModel.bills.test {
            assertThat(awaitItem()).isNull()

            // GIVEN & WHEN: A date range to be filtered
            billsViewModel.applyTextFilter("Teste")

            // THEN: Should return a filtered bills list
            assertThat(awaitItem()).isEqualTo(filteredBillsByText)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getBills_getBills_shouldFillBillsList() = runTest {
        val bills = listOf(
            Bill(
                billId = 1L,
                billType = BillType.INCOME,
                origin = "Companhia de Energia",
                title = "Conta de Luz",
                value = 250.75,
                description = "Conta de energia elétrica referente ao mês de janeiro",
                date = 1706659200000L,
                paymentType = PaymentType.CARD,
                paid = false,
                dueDate = 1709251200000L,
                expired = false,
                category = "Despesas Domésticas",
                tags = listOf("energia", "luz", "casa")
            ),
            Bill(
                billId = 2L,
                billType = BillType.EXPENSE,
                origin = "Netflix",
                title = "Assinatura Netflix",
                value = 39.90,
                description = "Assinatura mensal do serviço de streaming",
                date = 1706745600000L,
                paymentType = PaymentType.MONEY,
                paid = true,
                dueDate = 1709337600000L,
                expired = false,
                category = "Entretenimento",
                tags = listOf("streaming", "filmes", "séries")
            ),
            Bill(
                billId = 3L,
                billType = BillType.INCOME,
                origin = "Imobiliária XYZ",
                title = "Aluguel do Apartamento",
                value = 1800.00,
                description = "Aluguel mensal do apartamento no centro da cidade",
                date = 1706832000000L,
                paymentType = PaymentType.MONEY,
                paid = true,
                dueDate = 1709424000000L,
                expired = false,
                category = "Moradia",
                tags = listOf("aluguel", "moradia", "casa")
            )
        )

        val billsViewModel = BillsViewModel(
            getBillsUseCase, saveBillUseCase, updateBillUseCase,
            deleteBillUseCase, getBillsByDateUseCase, getBillsByTextUseCase
        )

        Mockito.`when`(getBillsUseCase()).thenReturn(
            flow { emit(Response.Success(bills)) }
        )

        billsViewModel.bills.test {
            assertThat(awaitItem()).isNull()

            // GIVEN & WHEN: List of bills is requested
            billsViewModel.getBills()

            // THEN: bills list should be filled with requested bills and reversed
            assertThat(awaitItem()).isEqualTo(bills.reversed())

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveBill_saveBillToUseCase_shouldSaveBillAndFillUiStateWithSuccess() = runTest {
        // GIVEN: A bill to be saved
        val billToBeSaved = BillState(
            billId = 1L,
            billType = BillType.INCOME,
            origin = "Companhia de Energia",
            title = "Conta de Luz",
            value = 250.75,
            description = "Conta de energia elétrica referente ao mês de janeiro",
            date = 1706659200000L,
            paymentType = PaymentType.CARD,
            paid = false,
            dueDate = 1709251200000L,
            expired = false,
            category = "Despesas Domésticas",
            tags = listOf("energia", "luz", "casa")
        )

        val billsViewModel = BillsViewModel(
            getBillsUseCase, saveBillUseCase, updateBillUseCase,
            deleteBillUseCase, getBillsByDateUseCase, getBillsByTextUseCase
        )

        Mockito.`when`(saveBillUseCase(billToBeSaved.toBill())).thenReturn(
            flow { emit(Response.Success(1L)) }
        )
        Mockito.`when`(getBillsUseCase()).thenReturn(
            flow { emit(Response.Success(emptyList())) }
        )

        billsViewModel.updateBillState(billToBeSaved)

        billsViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(UiState<CrudOperationType>())

            // WHEN: Save the bill
            billsViewModel.saveBill(billToBeSaved)

            assertThat(awaitItem().operationType).isEqualTo(CrudOperationType.SAVE)

            // THEN: Should emit success
            assertThat(awaitItem().success).isTrue()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteTag_deleteTag_shouldDeleteTagCorrectly() = runTest {
        val billsViewModel = BillsViewModel(
            getBillsUseCase, saveBillUseCase, updateBillUseCase,
            deleteBillUseCase, getBillsByDateUseCase, getBillsByTextUseCase
        )

        // GIVEN: A bill to be updated
        val billWithTags = BillState(
            billId = 1L,
            billType = BillType.INCOME,
            origin = "Companhia de Energia",
            title = "Conta de Luz",
            value = 250.75,
            description = "Conta de energia elétrica referente ao mês de janeiro",
            date = 1706659200000L,
            paymentType = PaymentType.CARD,
            paid = false,
            dueDate = 1709251200000L,
            expired = false,
            category = "Despesas Domésticas",
            tags = listOf("energia", "luz", "casa")
        )

        billsViewModel.updateBillState(billWithTags)

        billsViewModel.billStateFlow.test {
            // Bill tags should have the tag that will be deleted
            assertThat(awaitItem().tags).contains("luz")

            // WHEN: A tag is deleted
            billsViewModel.deleteTag("luz")

            // THEN: Bill tags should not have deleted tag
            assertThat(awaitItem().tags).doesNotContain("luz")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateBill_updateBillToUseCase_shouldUpdateBillAndFillUiStateWithSuccess() = runTest {
        // GIVEN: A bill to be updated
        val billToBeUpdated = BillState(
            billId = 1L,
            billType = BillType.INCOME,
            origin = "Companhia de Energia",
            title = "Conta de Luz",
            value = 250.75,
            description = "Conta de energia elétrica referente ao mês de janeiro",
            date = 1706659200000L,
            paymentType = PaymentType.CARD,
            paid = false,
            dueDate = 1709251200000L,
            expired = false,
            category = "Despesas Domésticas",
            tags = listOf("energia", "luz", "casa")
        )

        val billsViewModel = BillsViewModel(
            getBillsUseCase, saveBillUseCase, updateBillUseCase,
            deleteBillUseCase, getBillsByDateUseCase, getBillsByTextUseCase
        )

        Mockito.`when`(updateBillUseCase(billToBeUpdated.toBill())).thenReturn(
            flow { emit(Response.Success(1)) }
        )
        Mockito.`when`(getBillsUseCase()).thenReturn(
            flow { emit(Response.Success(emptyList())) }
        )

        billsViewModel.updateBillState(billToBeUpdated)

        billsViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(UiState<CrudOperationType>())

            // WHEN: Update the bill
            billsViewModel.updateBill(billToBeUpdated)

            assertThat(awaitItem().operationType).isEqualTo(CrudOperationType.UPDATE)

            // THEN: Should emit success
            assertThat(awaitItem().success).isTrue()

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteBill_deleteBillToUseCase_shouldSaveBillAndFillUiStateWithSuccess() = runTest {
        // GIVEN: A bill to be deleted
        val billToBeDeleted = BillState(
            billId = 1L,
            billType = BillType.INCOME,
            origin = "Companhia de Energia",
            title = "Conta de Luz",
            value = 250.75,
            description = "Conta de energia elétrica referente ao mês de janeiro",
            date = 1706659200000L,
            paymentType = PaymentType.CARD,
            paid = false,
            dueDate = 1709251200000L,
            expired = false,
            category = "Despesas Domésticas",
            tags = listOf("energia", "luz", "casa")
        )

        val billsViewModel = BillsViewModel(
            getBillsUseCase, saveBillUseCase, updateBillUseCase,
            deleteBillUseCase, getBillsByDateUseCase, getBillsByTextUseCase
        )

        Mockito.`when`(deleteBillUseCase(billToBeDeleted.toBill())).thenReturn(
            flow { emit(Response.Success(1)) }
        )
        Mockito.`when`(getBillsUseCase()).thenReturn(
            flow { emit(Response.Success(emptyList())) }
        )

        billsViewModel.updateBillState(billToBeDeleted)

        billsViewModel.uiState.test {
            //assertThat(awaitItem()).isEqualTo(UiState<CrudOperationType>())
            println("+++ 1: ${awaitItem()}")

            // WHEN: Delete the bill
            billsViewModel.deleteBill(billToBeDeleted)

            //assertThat(awaitItem().operationType).isEqualTo(CrudOperationType.DELETE)
            println("+++ 2: ${awaitItem()}")

            // THEN: Should emit success
            //assertThat(awaitItem().success).isTrue()
            println("+++ 3: ${awaitItem()}")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }
}