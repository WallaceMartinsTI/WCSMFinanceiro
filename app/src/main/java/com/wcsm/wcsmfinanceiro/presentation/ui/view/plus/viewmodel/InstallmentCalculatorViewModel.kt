package com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.viewmodel

import androidx.lifecycle.ViewModel
import com.wcsm.wcsmfinanceiro.presentation.model.plus.InstallmentCalculatorState
import com.wcsm.wcsmfinanceiro.util.toBrazilianReal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InstallmentCalculatorViewModel : ViewModel() {

    private val _installmentCalculatorStateFlow = MutableStateFlow(InstallmentCalculatorState())
    val installmentCalculatorStateFlow = _installmentCalculatorStateFlow.asStateFlow()

    fun updateInstallmentCalculatorStateFlow(installmentCalculatorState: InstallmentCalculatorState) {
        _installmentCalculatorStateFlow.value = installmentCalculatorState
    }

    fun resetInstallmentCalculatorStateFlow() {
        _installmentCalculatorStateFlow.value = InstallmentCalculatorState()
    }

    fun calculateInstallments() {
        if(isInstallmentCalculatorStateValid()) {
            val value = installmentCalculatorStateFlow.value.value
            val installment = installmentCalculatorStateFlow.value.installment
            val fees = installmentCalculatorStateFlow.value.fees

            val feesPercent = fees.toDoubleOrNull()?.let { it / 100 } ?: 0.0

            val totalFees = if(feesPercent > 0) {
                (value * feesPercent) * installment // J = C * i * t
            } else {
                0.0
            }

            val amount = value + totalFees // M = C + J
            val installmentValue = amount / installment

            val formattedResult = if(feesPercent > 0) {
                "${installmentValue.toBrazilianReal()} x$installment ($fees%/mês)"
            } else {
                "${installmentValue.toBrazilianReal()} x$installment"
            }

            updateInstallmentCalculatorStateFlow(
                installmentCalculatorStateFlow.value.copy(
                    installmentCalculationResult = formattedResult,
                    installmentTotalWithFees = amount.toBrazilianReal()
                )
            )
        }
    }

    private fun isInstallmentCalculatorStateValid() : Boolean {
        val fees = installmentCalculatorStateFlow.value.fees
        if(fees.endsWith(".")) {
            updateInstallmentCalculatorStateFlow(
                installmentCalculatorState = installmentCalculatorStateFlow.value.copy(
                    fees = fees.dropLast(1).trim()
                )
            )
        }

        val isValueValid = validateValue(installmentCalculatorStateFlow.value.value)
        val isInstallmentValid = validateInstallment(installmentCalculatorStateFlow.value.installment)
        val isFeesValid = validateFees(fees)

        updateInstallmentCalculatorStateFlow(
            installmentCalculatorStateFlow.value.copy(
                valueErrorMessage = isValueValid.second,
                installmentErrorMessage = isInstallmentValid.second,
                feesErrorMessage = isFeesValid.second
            )
        )

        return isValueValid.first && isInstallmentValid.first && isFeesValid.first
    }

    private fun validateValue(value: Double) : Pair<Boolean, String> {
        return if(value == 0.0) {
            Pair(false, "Você deve informar um valor maior que 0.")
        } else if(value < 0) {
            Pair(false, "Valor inválido.")
        } else {
            Pair(true, "")
        }
    }

    private fun validateInstallment(installment: Int) : Pair<Boolean, String> {
        return if(installment == 0) {
            Pair(false, "Você deve informar uma parcela maior 0.")
        } else if(installment < 0) {
            Pair(false, "Parcela inválido.")
        } else if(installment > 360) {
            Pair(false, "O número máximo de parcelas é 360.")
        } else {
            Pair(true, "")
        }
    }

    private fun validateFees(fees: String) : Pair<Boolean, String> {
        if (fees.isEmpty()) return Pair(true, "")

        val splitFees = fees.split(".")
        if (splitFees.size == 2 && splitFees[1].length > 2) {
            return Pair(false, "Permitido somente 2 casas decimais.")
        }

        val feesValue = fees.toDoubleOrNull() ?: return Pair(false, "Juros inválido, tente novamente (formato: 2,5)")
        return when {
            feesValue < 0 -> Pair(false, "Juros inválido.")
            feesValue > 500 -> Pair(false, "A porcentagem máxima do juros é 500%.")
            else -> Pair(true, "")
        }
    }
}