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
                    installmentCalculationResult = formattedResult
                )
            )
        }
    }

    private fun isInstallmentCalculatorStateValid() : Boolean {
        val isValueValid = validateValue(installmentCalculatorStateFlow.value.value)
        val isInstallmentValid = validateInstallment(installmentCalculatorStateFlow.value.installment)
        val isFeesValid = validateFees(installmentCalculatorStateFlow.value.fees)

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
        return try {
           if(fees.toDouble() < 0) {
                Pair(false, "Juros inválido.")
            } else if(fees.toDouble() > 500) {
                Pair(false, "A porcentagem máxima do juros é 500%.")
            } else if(fees.isBlank() || fees.toDoubleOrNull() != null) {
                Pair(true, "")
            } else {
                Pair(false, "Erro: $fees")
            }
        } catch (e: Exception) {
            Pair(false, "Juros inválido, tente novamente (formato: 2,5)")
        }

    }
}