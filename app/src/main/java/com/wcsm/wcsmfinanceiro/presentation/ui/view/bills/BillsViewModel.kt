package com.wcsm.wcsmfinanceiro.presentation.ui.view.bills

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.DeleteBillUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.GetBillsByDateUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.GetBillsByTextUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.GetBillsUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.SaveBillUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.UpdateBillUseCase
import com.wcsm.wcsmfinanceiro.presentation.model.BillOperationType
import com.wcsm.wcsmfinanceiro.presentation.model.BillState
import com.wcsm.wcsmfinanceiro.presentation.model.WalletOperationType
import com.wcsm.wcsmfinanceiro.presentation.model.UiState
import com.wcsm.wcsmfinanceiro.presentation.util.toBill
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
    // Temp for tests
    /*private val billsList = listOf(
        Bill(
            id = 1,
            billType = BillType.INCOME,
            origin = "Trabalho",
            title = "Salário",
            value = 2624.72,
            description = "Salário do mês de Janeiro",
            date = 1736208000000, // Mon Jan 06 2025 21:00:00.000
            category = Category(1, "Trabalho"),
            dueDate = 1739588400000, // Sat Feb 15 2025 00:00:00.000
            expired = false,
            paid = true,
            paymentType = PaymentType.MONEY,
            tags = listOf("Salario", "Trabalho")
        ),
        Bill(
            id = 2,
            billType = BillType.EXPENSE,
            origin = "Mercado",
            title = "Compra do Mês Data",
            value = 975.35,
            description = "Compra do mês de Janeiro",
            date = 1736698430000, // Mon Jan 06 2025 21:00:00.000
            category = Category(1, "Mercado"),
            dueDate = 1736698430000, // Sat Feb 15 2025 00:00:00.000
            expired = false,
            paid = true,
            paymentType = PaymentType.CARD,
            tags = listOf("Mercado")
        ),
        Bill(
            id = 3,
            billType = BillType.INCOME,
            origin = "Trabalho",
            title = "Salário",
            value = 2624.72,
            description = "Salário do mês de Janeiro",
            date = 1736208000000, // Mon Jan 06 2025 21:00:00.000
            category = Category(1, "Trabalho"),
            dueDate = 1739588400000, // Sat Feb 15 2025 00:00:00.000
            expired = false,
            paid = true,
            paymentType = PaymentType.MONEY,
            tags = listOf("Salario", "Trabalho")
        ),
        Bill(
            id = 4,
            billType = BillType.EXPENSE,
            origin = "Mercado",
            title = "Compra do Mês",
            value = 975.35,
            description = "Compra do mês de Janeiro",
            date = 1736208000000, // Mon Jan 06 2025 21:00:00.000
            category = Category(1, "Mercado"),
            dueDate = 1739588400000, // Sat Feb 15 2025 00:00:00.000
            expired = false,
            paid = true,
            paymentType = PaymentType.CARD,
            tags = listOf("Mercado")
        ),
        Bill(
            id = 5,
            billType = BillType.INCOME,
            origin = "Trabalho",
            title = "Salário Data",
            value = 2624.72,
            description = "Salário do mês de Janeiro",
            date = 1736698430000, // Mon Jan 06 2025 21:00:00.000
            category = Category(1, "Trabalho"),
            dueDate = 1736698430000, // Sat Feb 15 2025 00:00:00.000
            expired = false,
            paid = true,
            paymentType = PaymentType.MONEY,
            tags = listOf("Salario", "Trabalho")
        ),
        Bill(
            id = 6,
            billType = BillType.EXPENSE,
            origin = "Mercado",
            title = "Compra do Mês",
            value = 975.35,
            description = "Compra do mês de Janeiro",
            date = 1736208000000, // Mon Jan 06 2025 21:00:00.000
            category = Category(1, "Mercado"),
            dueDate = 1739588400000, // Sat Feb 15 2025 00:00:00.000
            expired = false,
            paid = true,
            paymentType = PaymentType.CARD,
            tags = listOf("Mercado")
        ),
        Bill(
            id = 7,
            billType = BillType.INCOME,
            origin = "Trabalho",
            title = "Salário",
            value = 2624.72,
            description = "Salário do mês de Janeiro",
            date = 1736208000000, // Mon Jan 06 2025 21:00:00.000
            category = Category(1, "Trabalho"),
            dueDate = 1739588400000, // Sat Feb 15 2025 00:00:00.000
            expired = false,
            paid = true,
            paymentType = PaymentType.MONEY,
            tags = listOf("Salario", "Trabalho")
        ),
        Bill(
            id = 8,
            billType = BillType.EXPENSE,
            origin = "Mercado",
            title = "Compra do Mês",
            value = 975.35,
            description = "Compra do mês de Janeiro",
            date = 1736208000000, // Mon Jan 06 2025 21:00:00.000
            category = Category(1, "Mercado"),
            dueDate = 1739588400000, // Sat Feb 15 2025 00:00:00.000
            expired = false,
            paid = true,
            paymentType = PaymentType.CARD,
            tags = listOf("Mercado")
        ),
        Bill(
            id = 9,
            billType = BillType.INCOME,
            origin = "Trabalho",
            title = "Salário",
            value = 2624.72,
            description = "Salário do mês de Janeiro",
            date = 1736208000000, // Mon Jan 06 2025 21:00:00.000
            category = Category(1, "Trabalho"),
            dueDate = 1739588400000, // Sat Feb 15 2025 00:00:00.000
            expired = false,
            paid = true,
            paymentType = PaymentType.MONEY,
            tags = listOf("Salario", "Trabalho")
        ),
        Bill(
            id = 10,
            billType = BillType.EXPENSE,
            origin = "Mercado",
            title = "Compra do Mês",
            value = 975.35,
            description = "Compra do mês de Janeiro",
            date = 1736208000000, // Mon Jan 06 2025 21:00:00.000
            category = Category(1, "Mercado"),
            dueDate = 1739588400000, // Sat Feb 15 2025 00:00:00.000
            expired = false,
            paid = true,
            paymentType = PaymentType.CARD,
            tags = listOf("Mercado")
        ),
        Bill(
            id = 11,
            billType = BillType.INCOME,
            origin = "Trabalho",
            title = "Salário",
            value = 2624.72,
            description = "Salário do mês de Janeiro",
            date = 1736208000000, // Mon Jan 06 2025 21:00:00.000
            category = Category(1, "Trabalho"),
            dueDate = 1739588400000, // Sat Feb 15 2025 00:00:00.000
            expired = false,
            paid = true,
            paymentType = PaymentType.MONEY,
            tags = listOf("Salario", "Trabalho")
        ),
        Bill(
            id = 12,
            billType = BillType.EXPENSE,
            origin = "Mercado",
            title = "Compra do Mês",
            value = 975.35,
            description = "Compra do mês de Janeiro",
            date = 1736208000000, // Mon Jan 06 2025 21:00:00.000
            category = Category(1, "Mercado"),
            dueDate = 1739588400000, // Sat Feb 15 2025 00:00:00.000
            expired = false,
            paid = true,
            paymentType = PaymentType.CARD,
            tags = listOf("Mercado")
        ),
        Bill(
            id = 13,
            billType = BillType.INCOME,
            origin = "Trabalho",
            title = "Salário",
            value = 2624.72,
            description = "Salário do mês de Janeiro",
            date = 1736208000000, // Mon Jan 06 2025 21:00:00.000
            category = Category(1, "Trabalho"),
            dueDate = 1739588400000, // Sat Feb 15 2025 00:00:00.000
            expired = false,
            paid = true,
            paymentType = PaymentType.MONEY,
            tags = listOf("Salario", "Trabalho")
        ),
        Bill(
            id = 14,
            billType = BillType.EXPENSE,
            origin = "Mercado",
            title = "Compra do Mês",
            value = 975.35,
            description = "Compra do mês de Janeiro",
            date = 1736208000000, // Mon Jan 06 2025 21:00:00.000
            category = Category(1, "Mercado"),
            dueDate = 1739588400000, // Sat Feb 15 2025 00:00:00.000
            expired = false,
            paid = true,
            paymentType = PaymentType.CARD,
            tags = listOf("Mercado")
        ),
        Bill(
            id = 15,
            billType = BillType.INCOME,
            origin = "Trabalho",
            title = "Salário",
            value = 2624.72,
            description = "Salário do mês de Janeiro",
            date = 1736208000000, // Mon Jan 06 2025 21:00:00.000
            category = Category(1, "Trabalho"),
            dueDate = 1739588400000, // Sat Feb 15 2025 00:00:00.000
            expired = false,
            paid = true,
            paymentType = PaymentType.MONEY,
            tags = listOf("Salario", "Trabalho")
        ),
        Bill(
            id = 16,
            billType = BillType.EXPENSE,
            origin = "Mercado",
            title = "Compra do Mês",
            value = 975.35,
            description = "Compra do mês de Janeiro",
            date = 1736208000000, // Mon Jan 06 2025 21:00:00.000
            category = Category(1, "Mercado"),
            dueDate = 1739588400000, // Sat Feb 15 2025 00:00:00.000
            expired = false,
            paid = true,
            paymentType = PaymentType.CARD,
            tags = listOf("Mercado")
        ),
    )*/

    private val _uiState = MutableStateFlow(UiState<BillOperationType>())
    val uiState = _uiState.asStateFlow()

    private val _billStateFlow = MutableStateFlow(BillState())
    val billStateFlow = _billStateFlow.asStateFlow()

    private val _filterSelectedDateRange = MutableStateFlow<Pair<Long, Long>?>(null)
    val filterSelectedDateRange = _filterSelectedDateRange.asStateFlow()

    private val _bills = MutableStateFlow<List<Bill>?>(null)
    val bills = _bills.asStateFlow()

    init {
        getBills()
    }

    fun updateFilterSelectedDateRange(filterSelectedRange: Pair<Long, Long>?) {
        _filterSelectedDateRange.value = filterSelectedRange
    }

    fun updateBillState(updatedState: BillState) {
        _billStateFlow.value = updatedState
    }

    fun resetBillState() {
        _billStateFlow.value = BillState()
    }

    fun resetUiState() {
        _uiState.value = UiState()
    }

    fun applyDateRangeFilter(startDate: Long, endDate: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(
                operationType = null
            )

            getBillsByDateUseCase(startDate, endDate).collect { result ->
                when(result) {
                    is Response.Loading -> {
                        _uiState.value = uiState.value.copy(
                            isLoading = true,
                        )
                    }
                    is Response.Error -> {
                        // SHOW ERROR MESSAGE result.message
                        _uiState.value = uiState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                    is Response.Success -> {
                        _bills.value = result.data.sortedBy { bill ->
                            bill.date
                        }

                        _uiState.value = uiState.value.copy(
                            isLoading = false,
                            success = true
                        )
                    }
                }
            }
        }
    }

    fun applyTextFilter(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(
                operationType = null
            )

            getBillsByTextUseCase(text).collect { result ->
                when(result) {
                    is Response.Loading -> {
                        _uiState.value = uiState.value.copy(
                            isLoading = true,
                        )
                    }
                    is Response.Error -> {
                        // SHOW ERROR MESSAGE result.message
                        _uiState.value = uiState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                    is Response.Success -> {
                        _bills.value = result.data

                        _uiState.value = uiState.value.copy(
                            isLoading = false,
                            success = true
                        )
                    }
                }
            }
        }
    }

    fun clearFilter() {
        _filterSelectedDateRange.value = null
        getBills()
    }

    private fun getBills() {
        viewModelScope.launch(Dispatchers.IO) {
            getBillsUseCase().collect { result ->
                when(result) {
                    is Response.Loading -> {
                        _uiState.value = uiState.value.copy(
                            isLoading = true
                        )
                    }
                    is Response.Error -> {
                        // SHOW ERROR MESSAGE result.message
                        _uiState.value = uiState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                    is Response.Success -> {
                        _bills.value = result.data.reversed()
                        _uiState.value = uiState.value.copy(
                            isLoading = false,
                            success = true
                        )
                    }
                }
            }
        }
    }

    fun saveBill(billState: BillState) {
        resetErrorMessages()

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(
                operationType = BillOperationType.SAVE
            )

            if(isBillStateValid()) {
                val bill = billState.toBill()
                saveBillUseCase(bill).collect { result ->
                    when(result) {
                        is Response.Loading -> {
                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                            )
                        }
                        is Response.Error -> {
                            // SHOW ERROR MESSAGE result.message
                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                        is Response.Success -> {
                            _billStateFlow.value = BillState()

                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                                success = true
                            )

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
        resetErrorMessages()

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(
                operationType = BillOperationType.UPDATE
            )

            if(isBillStateValid()) {
                val bill = billState.toBill()
                updateBillUseCase(bill).collect { result ->
                    when(result) {
                        is Response.Loading -> {
                            _uiState.value = uiState.value.copy(
                                isLoading = true
                            )
                        }
                        is Response.Error -> {
                            // SHOW ERROR MESSAGE result.message
                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                        is Response.Success -> {
                            if(!isUpdatingOnlyTags) {
                                _billStateFlow.value = BillState()

                                _uiState.value = uiState.value.copy(
                                    isLoading = false,
                                    success = true
                                )

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
            _uiState.value = uiState.value.copy(
                operationType = BillOperationType.DELETE
            )

            if(isBillStateValid()) {
                val bill = billState.toBill()
                deleteBillUseCase(bill).collect { result ->
                    when(result) {
                        is Response.Loading -> {
                            _uiState.value = uiState.value.copy(
                                isLoading = true
                            )
                        }
                        is Response.Error -> {
                            // SHOW ERROR MESSAGE result.message
                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                        is Response.Success -> {
                            _billStateFlow.value = BillState()

                            _uiState.value = uiState.value.copy(
                                isLoading = false,
                                success = true
                            )

                            getBills()
                        }
                    }
                }
            }
        }
    }

    private fun resetErrorMessages() {
        updateBillState(
            billStateFlow.value.copy(
                titleErrorMessage = "",
                dateErrorMessage = "",
                valueErrorMessage = "",
                categoryErrorMessage = ""
            )
        )
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
        } else if(title.length > 50) {
            Pair(false, "O título é muito grande (max. 50 caracteres).")
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
