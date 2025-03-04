package com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.usecase.plus.GetConvertedCurrencyUseCase
import com.wcsm.wcsmfinanceiro.presentation.model.UiState
import com.wcsm.wcsmfinanceiro.presentation.model.bills.BillOperationType
import com.wcsm.wcsmfinanceiro.presentation.model.plus.CurrencyConversionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConversionViewModel @Inject constructor(
    private val getConvertedCurrencyUseCase: GetConvertedCurrencyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState<Nothing>())
    val uiState = _uiState.asStateFlow()

    private val _currencyConversionStateFlow = MutableStateFlow(CurrencyConversionState())
    val currencyConversionStateFlow = _currencyConversionStateFlow.asStateFlow()

    fun updateCurrencyConversionStateFlow(currencyConversionState: CurrencyConversionState) {
        _currencyConversionStateFlow.value = currencyConversionState
    }

    fun resetUiState() {
        _uiState.value = UiState()
    }

    private fun updateUiState(uiState: UiState<Nothing>) {
        _uiState.value = uiState
    }

    private fun onLoadingResponse() {
        updateUiState(uiState.value.copy(isLoading = true))
    }

    private fun onErrorResponse(errorMessage: String) {
        updateUiState(uiState.value.copy(isLoading = false, error = errorMessage))
    }

    private fun onSuccessResponse(onSuccess: () -> Unit) {
        onSuccess()

        updateUiState(
            uiState.value.copy(
                isLoading = false,
                success = true
            )
        )
    }

    fun getConvertedCurrency(currencyConversionState: CurrencyConversionState) {
        viewModelScope.launch(Dispatchers.IO) {
            if(isCurrencyConversionStateValid()) {
                val baseCode = currencyConversionState.baseCode.split("-")[0].trim()
                val targetCode = currencyConversionState.targetCode.split("-")[0].trim()
                val valueToConvert = currencyConversionState.valueToConvert

                getConvertedCurrencyUseCase(baseCode, targetCode, valueToConvert).collect { result ->
                    when(result) {
                        is Response.Loading -> onLoadingResponse()
                        is Response.Error -> onErrorResponse(result.message)
                        is Response.Success -> onSuccessResponse {
                            _currencyConversionStateFlow.value = currencyConversionStateFlow.value.copy(
                                convertedValue = result.data
                            )
                        }
                    }
                }
            }
        }
    }

    private fun isCurrencyConversionStateValid() : Boolean {
        val isBaseCodeValid = validateBaseCode(currencyConversionStateFlow.value.baseCode)
        val isTargetCodeValid = validateTargetCode(currencyConversionStateFlow.value.targetCode)
        val isValueToConvertValid = validateValueToConvert(currencyConversionStateFlow.value.valueToConvert)

        updateCurrencyConversionStateFlow(
            currencyConversionStateFlow.value.copy(
                baseCodeErrorMessage = isBaseCodeValid.second,
                targetCodeErrorMessage = isTargetCodeValid.second,
                valueToConvertErrorMessage = isValueToConvertValid.second
            )
        )

        return isBaseCodeValid.first && isTargetCodeValid.first && isValueToConvertValid.first
    }

    private fun validateValueToConvert(value: Double) : Pair<Boolean, String> {
        return if(value == 0.0) {
            Pair(false, "Você deve informar um valor maior que 0.")
        } else if(value < 0) {
            Pair(false, "Valor inválido.")
        } else {
            Pair(true, "")
        }
    }

    private fun validateBaseCode(baseCode: String) : Pair<Boolean, String> {
        return if(baseCode.isBlank() || baseCode == "Selecione uma moeda") {
            Pair(false, "Você deve selecionar uma moeda.")
        } else {
            Pair(true, "")
        }
    }

    private fun validateTargetCode(targetCode: String) : Pair<Boolean, String> {
        return if(targetCode.isBlank() || targetCode == "Selecione uma moeda") {
            Pair(false, "Você deve selecionar uma moeda.")
        } else {
            Pair(true, "")
        }
    }
}