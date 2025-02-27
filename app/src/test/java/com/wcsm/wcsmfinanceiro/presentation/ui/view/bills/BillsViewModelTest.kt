package com.wcsm.wcsmfinanceiro.presentation.ui.view.bills

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.DeleteBillUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.GetBillsByDateUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.GetBillsByTextUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.GetBillsUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.SaveBillUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.UpdateBillUseCase
import com.wcsm.wcsmfinanceiro.presentation.model.UiState
import com.wcsm.wcsmfinanceiro.presentation.model.bills.BillOperationType
import com.wcsm.wcsmfinanceiro.presentation.model.bills.BillState
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class BillsViewModelTest {

    private lateinit var billsViewModel: BillsViewModel

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

        billsViewModel = BillsViewModel(
            getBillsUseCase, saveBillUseCase, updateBillUseCase,
            deleteBillUseCase, getBillsByDateUseCase, getBillsByTextUseCase
        )
    }

    @Test
    fun updateFilterSelectedDateRange_updatingFilterSelectedDateRangeState_shouldUpdateCorrectly() = runTest {
        // GIVEN: A date range to filter
        val dateRange = Pair(1000L, 2000L)

        billsViewModel.filterSelectedDateRange.test {
            // At first filterSelectedDateRange state should start with null value
            assertThat(awaitItem()).isNull()

            // WHEN: A date is passed to be filtered
            billsViewModel.updateFilterSelectedDateRange(dateRange)

            // THEN: filterSelectedDateRange state should match with passed dateRange
            assertThat(awaitItem()).isEqualTo(dateRange)
        }
    }

    @Test
    fun updateBillState_updateBillState_shouldUpdateCorrectly() = runTest {
        // GIVEN: A new billState to be updated
        val billState = BillState()

        billsViewModel.billStateFlow.test {
            // At first billStateFlow state should start with an empty BillState
            assertThat(awaitItem()).isEqualTo(billState)

            // WHEN: Passed a new bill state to be updated
            billsViewModel.updateBillState(billState.copy(title = "Título Atualizado"))

            // THEN: Bill state should match with updated BillState
            assertThat(awaitItem().title).isEqualTo("Título Atualizado")
        }
    }

    @Test
    fun resetBillState_resetBillState_shouldMatchWithAndEmptyBillState() = runTest {
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
        }
    }

    @Test
    fun resetUiState_resetUiState_shouldMatchWithAnEmptyUiState() = runTest {
        // GIVEN: A reset ui state
        var uiState = UiState<BillOperationType>()

        billsViewModel.uiState.test {
            // At first uiState state should start with an empty UiState
            assertThat(awaitItem()).isEqualTo(uiState)

            // Update uiState to be reset
            uiState = uiState.copy(error = "Erro simulado")
            assertThat(awaitItem().error).isEqualTo("Erro simulado")

            billsViewModel.resetUiState()

            assertThat(awaitItem()).isEqualTo(uiState)
        }
    }

    ORGANIZAR CODIGOS DA VIEWMODEL E DO TESTE

    /*
        @Test
        fun getUiState() {
        }

        @Test
        fun getBillStateFlow() {
        }

        @Test
        fun getFilterSelectedDateRange() {
        }

        @Test
        fun getBills() {
        }




        @Test
        fun applyDateRangeFilter() {
        }

        @Test
        fun applyTextFilter() {
        }

        @Test
        fun clearFilter() {
        }

        @Test
        fun saveBill() {
        }

        @Test
        fun deleteTag() {
        }

        @Test
        fun updateBill() {
        }

        @Test
        fun deleteBill() {
        }
        */
}