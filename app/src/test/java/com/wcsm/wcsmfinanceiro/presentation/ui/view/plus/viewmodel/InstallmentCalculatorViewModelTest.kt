package com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.presentation.model.plus.InstallmentCalculatorState
import com.wcsm.wcsmfinanceiro.util.toBrazilianReal
import kotlinx.coroutines.test.runTest
import org.junit.Test

class InstallmentCalculatorViewModelTest {

    @Test
    fun updateInstallmentCalculatorStateFlow_updateInstallmentCalculatorStateFlow_shouldUpdateCorrectly() = runTest {
        val expectedInstallmentCalculatorState = InstallmentCalculatorState(
            value = 1000.0,
            installment = 5,
            fees = "2",
            installmentCalculationResult = "225",
            installmentTotalWithFees = "1450"
        )

        val installmentCalculatorViewModel = InstallmentCalculatorViewModel()

        // GIVEN: A installment calculator state to be updated
        val installmentCalculatorState = InstallmentCalculatorState()

        installmentCalculatorViewModel.installmentCalculatorStateFlow.test {
            assertThat(awaitItem()).isEqualTo(installmentCalculatorState)

            // WHEN: Update installment calculator state
            installmentCalculatorViewModel.updateInstallmentCalculatorStateFlow(
                installmentCalculatorState.copy(
                    value = 1000.0,
                    installment = 5,
                    fees = "2",
                    installmentCalculationResult = "225",
                    installmentTotalWithFees = "1450"
                )
            )

            // THEN: Installment calculator state should match with expected
            assertThat(awaitItem()).isEqualTo(expectedInstallmentCalculatorState)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun resetInstallmentCalculatorStateFlow_resetInstallmentCalculatorStateFlow_shouldMatchWithAnEmptyInstallmentCalculatorState() = runTest {
        val installmentCalculatorViewModel = InstallmentCalculatorViewModel()

        // GIVEN: A reset installment calculator state
        val installmentCalculatorState = InstallmentCalculatorState()

        installmentCalculatorViewModel.installmentCalculatorStateFlow.test {
            // At first installmentCalculator state should start with an empty InstallmentCalculatorState
            assertThat(awaitItem()).isEqualTo(installmentCalculatorState)

            // Update a installment calculator state to be reset
            installmentCalculatorViewModel.updateInstallmentCalculatorStateFlow(
                installmentCalculatorState.copy(
                    value = 1000.0,
                    installment = 5,
                    fees = "2",
                    installmentCalculationResult = "225",
                    installmentTotalWithFees = "1450"
                )
            )

            // Check if installment calculator state was updated
            val updatedInstallmentCalculatorState = awaitItem()
            assertThat(updatedInstallmentCalculatorState.value).isEqualTo(1000.0)
            assertThat(updatedInstallmentCalculatorState.installment).isEqualTo(5)
            assertThat(updatedInstallmentCalculatorState.fees).isEqualTo("2")
            assertThat(updatedInstallmentCalculatorState.installmentCalculationResult).isEqualTo("225")
            assertThat(updatedInstallmentCalculatorState.installmentTotalWithFees).isEqualTo("1450")

            // WHEN: Reset installment calculator state
            installmentCalculatorViewModel.resetInstallmentCalculatorStateFlow()

            // THEN: Installment calculator state should match with a reset installmentCalculatorState
            assertThat(awaitItem()).isEqualTo(installmentCalculatorState)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun calculateInstallments_calculateInstallmentsNoFees_shouldCalculateCorrectly() = runTest {
        val expectedResult = "${200.0.toBrazilianReal()} x5"

        val installmentCalculatorViewModel = InstallmentCalculatorViewModel()

        // GIVEN: InstallmentCalculatorState with values to calculate
        val installmentCalculatorState = InstallmentCalculatorState(
            value = 1000.0,
            installment = 5
        )

        installmentCalculatorViewModel.updateInstallmentCalculatorStateFlow(installmentCalculatorState)

        installmentCalculatorViewModel.installmentCalculatorStateFlow.test {
            assertThat(awaitItem()).isEqualTo(installmentCalculatorState)

            // WHEN: Calls calculate method from viewmodel
            installmentCalculatorViewModel.calculateInstallments()

            // THEN: Should match with expected result
            assertThat(awaitItem().installmentCalculationResult).isEqualTo(expectedResult)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun calculateInstallments_calculateInstallmentsWithFees_shouldCalculateCorrectly() = runTest {
        val expectedResult = "${225.0.toBrazilianReal()} x5 (2.5%/mês)"
        val expectedTotalResult = 1125.0.toBrazilianReal()

        val installmentCalculatorViewModel = InstallmentCalculatorViewModel()

        // GIVEN: InstallmentCalculatorState with values to calculate
        val installmentCalculatorState = InstallmentCalculatorState(
            value = 1000.0,
            installment = 5,
            fees = "2.5"
        )

        installmentCalculatorViewModel.updateInstallmentCalculatorStateFlow(installmentCalculatorState)

        installmentCalculatorViewModel.installmentCalculatorStateFlow.test {
            assertThat(awaitItem()).isEqualTo(installmentCalculatorState)

            // WHEN: Calls calculate method from viewmodel
            installmentCalculatorViewModel.calculateInstallments()

            // THEN: Should match with expected result
            val calculationResult = awaitItem()
            assertThat(calculationResult.installmentCalculationResult).isEqualTo(expectedResult)
            assertThat(calculationResult.installmentTotalWithFees).isEqualTo(expectedTotalResult)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }
}