package com.wcsm.wcsmfinanceiro.presentation.ui.view.bills

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wcsm.wcsmfinanceiro.data.local.entity.Bill
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.DeleteBillUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.GetBillsByDateUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.GetBillsByTextUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.GetBillsUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.SaveBillUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.UpdateBillUseCase
import com.wcsm.wcsmfinanceiro.presentation.model.bills.BillOperationType
import com.wcsm.wcsmfinanceiro.presentation.model.bills.BillState
import com.wcsm.wcsmfinanceiro.presentation.model.UiState
import com.wcsm.wcsmfinanceiro.util.toBill
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillsViewModel @Inject constructor(
    private val getBillsUseCase: GetBillsUseCase,
    private val saveBillUseCase: SaveBillUseCase,
    private val updateBillUseCase: UpdateBillUseCase,
    private val deleteBillUseCase: DeleteBillUseCase,
    private val getBillsByDateUseCase: GetBillsByDateUseCase,
    private val getBillsByTextUseCase: GetBillsByTextUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState<BillOperationType>())
    val uiState = _uiState.asStateFlow()

    private val _billStateFlow = MutableStateFlow(BillState())
    val billStateFlow = _billStateFlow.asStateFlow()

    private val _filterSelectedDateRange = MutableStateFlow<Pair<Long, Long>?>(null)
    val filterSelectedDateRange = _filterSelectedDateRange.asStateFlow()

    private val _bills = MutableStateFlow<List<Bill>?>(null)
    val bills = _bills.asStateFlow()

    fun updateFilterSelectedDateRange(filterSelectedRange: Pair<Long, Long>?) {
        _filterSelectedDateRange.value = filterSelectedRange
    }

    fun updateBillState(updatedState: BillState) {
        _billStateFlow.value = updatedState
    }

    fun updateUiState(uiState: UiState<BillOperationType>) {
        _uiState.value = uiState
    }

    fun clearSelectedDateRangeFilter() {
        _filterSelectedDateRange.value = null
        getBills()
    }

    fun resetBillState() {
        _billStateFlow.value = BillState()
    }

    fun resetUiState() {
        _uiState.value = UiState()
    }

    fun resetBillStateErrorMessages() {
        updateBillState(
            billStateFlow.value.copy(
                titleErrorMessage = "",
                dateErrorMessage = "",
                valueErrorMessage = "",
                categoryErrorMessage = ""
            )
        )
    }

    private fun onLoadingResponse() {
        updateUiState(uiState.value.copy(isLoading = true))
    }

    private fun onErrorResponse(errorMessage: String) {
        updateUiState(uiState.value.copy(isLoading = false, error = errorMessage))
    }

    private fun onSuccessResponse(onSuccess: () -> Unit) {
        onSuccess()

        updateUiState(uiState.value.copy(
            isLoading = false,
            success = true
        ))
    }

    fun applyDateRangeFilter(startDate: Long, endDate: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            updateUiState(uiState.value.copy(operationType = null))

            getBillsByDateUseCase(startDate, endDate).collect { result ->
                when(result) {
                    is Response.Loading -> onLoadingResponse()
                    is Response.Error -> onErrorResponse(result.message)
                    is Response.Success -> onSuccessResponse {
                        _bills.value = result.data.sortedBy { bill ->
                            bill.date
                        }
                    }
                }
            }
        }
    }

    fun applyTextFilter(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updateUiState(uiState.value.copy(operationType = null))

            getBillsByTextUseCase(text).collect { result ->
                when(result) {
                    is Response.Loading -> onLoadingResponse()
                    is Response.Error -> onErrorResponse(result.message)
                    is Response.Success -> onSuccessResponse {
                        _bills.value = result.data
                    }
                }
            }
        }
    }

    fun getBills() {
        viewModelScope.launch(Dispatchers.IO) {
            getBillsUseCase().collect { result ->
                when(result) {
                    is Response.Loading -> onLoadingResponse()
                    is Response.Error -> onErrorResponse(result.message)
                    is Response.Success -> onSuccessResponse {
                        _bills.value = result.data.reversed()
                    }
                }
            }
        }
    }

    fun saveBill(billState: BillState) {
        resetBillStateErrorMessages()

        viewModelScope.launch(Dispatchers.IO) {
            updateUiState(uiState.value.copy(operationType = BillOperationType.SAVE))

            if(isBillStateValid()) {
                val bill = billState.toBill()
                saveBillUseCase(bill).collect { result ->
                    when(result) {
                        is Response.Loading -> onLoadingResponse()
                        is Response.Error -> onErrorResponse(result.message)
                        is Response.Success -> onSuccessResponse {
                            _billStateFlow.value = BillState()
                            getBills()
                        }
                    }
                }
            }
        }
    }

    fun deleteTag(tag: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _billStateFlow.value
            updateBillState(
                currentState.copy(
                    tags = currentState.tags.filter {
                        it != tag
                    }
                )
            )
        }
    }

    fun updateBill(billState: BillState, isUpdatingOnlyTags: Boolean = false) {
        resetBillStateErrorMessages()

        viewModelScope.launch(Dispatchers.IO) {
            updateUiState(uiState.value.copy(operationType = BillOperationType.UPDATE))

            if(isBillStateValid()) {
                val bill = billState.toBill()
                updateBillUseCase(bill).collect { result ->
                    when(result) {
                        is Response.Loading -> onLoadingResponse()
                        is Response.Error -> onErrorResponse(result.message)
                        is Response.Success -> onSuccessResponse {
                            if(!isUpdatingOnlyTags) {
                                _billStateFlow.value = BillState()
                                getBills()
                            }
                        }
                    }
                }
            }
        }
    }

    fun deleteBill(billState: BillState) {
        viewModelScope.launch(Dispatchers.IO) {
            updateUiState(uiState.value.copy(operationType = BillOperationType.DELETE))

            if(isBillStateValid()) {
                val bill = billState.toBill()
                deleteBillUseCase(bill).collect { result ->
                    when(result) {
                        is Response.Loading -> onLoadingResponse()
                        is Response.Error -> onErrorResponse(result.message)
                        is Response.Success -> onSuccessResponse {
                            _billStateFlow.value = BillState()
                            getBills()
                        }
                    }
                }
            }
        }
    }

    private fun isBillStateValid() : Boolean {
        val isTitleValid = validateTitle(billStateFlow.value.title)
        val isDateValid = validateDate(billStateFlow.value.date)
        val isValueValid = validateValue(billStateFlow.value.value)
        val isCategoryValid = validateCategory(billStateFlow.value.category)

        updateBillState(
            billStateFlow.value.copy(
                titleErrorMessage = isTitleValid.second,
                dateErrorMessage = isDateValid.second,
                valueErrorMessage = isValueValid.second,
                categoryErrorMessage = isCategoryValid.second
            )
        )

        return isTitleValid.first && isDateValid.first && isValueValid.first && isCategoryValid.first
    }

    private fun validateTitle(title: String) : Pair<Boolean, String> {
        return if(title.isBlank()) {
            Pair(false, "O título não pode ser vazio.")
        } else if(title.length < 3) {
            Pair(false, "O título é muito curto (min. 3 caracteres).")
        } else if(title.length > 30) {
            Pair(false, "O título é muito grande (${title.length}/30 caracteres).")
        } else {
            Pair(true, "")
        }
    }

    private fun validateDate(date: Long) : Pair<Boolean, String> {
        return if(date == 0L) {
            Pair(false, "Você deve escolher uma data.")
        } else if(date < 0) {
            Pair(false, "Data inválida.")
        } else {
            Pair(true, "")
        }
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

    private fun validateCategory(category: String) : Pair<Boolean, String> {
        return if(category.isBlank() || category == "Selecione uma categoria") {
            Pair(false, "Você deve selecionar uma categoria.")
        } else {
            Pair(true, "")
        }
    }
}
