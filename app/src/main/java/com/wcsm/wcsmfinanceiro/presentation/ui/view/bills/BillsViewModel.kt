package com.wcsm.wcsmfinanceiro.presentation.ui.view.bills

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.domain.usecase.DeleteBillUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.GetBillsByDateUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.GetBillsByTextUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.GetBillsUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.SaveBillUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.UpdateBillUseCase
import com.wcsm.wcsmfinanceiro.presentation.model.BillState
import com.wcsm.wcsmfinanceiro.presentation.util.toBill
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _billDialogState = MutableStateFlow(BillState())
    val billDialogState: StateFlow<BillState> = _billDialogState

    private val _filterSelectedDateRange = MutableStateFlow<Pair<Long, Long>?>(null)
    val filterSelectedDateRange: StateFlow<Pair<Long, Long>?> = _filterSelectedDateRange

    private val _bills = MutableStateFlow<List<Bill>?>(null)
    val bills: StateFlow<List<Bill>?> = _bills

    private val _isAddOrEditSuccess = MutableStateFlow(false)
    val isAddOrEditSuccess: StateFlow<Boolean> = _isAddOrEditSuccess

    private val _isBillDeleted = MutableStateFlow(false)
    val isBillDeleted: StateFlow<Boolean> = _isBillDeleted

    init {
        getBills()
    }

    fun updateFilterSelectedDateRange(filterSelectedRange: Pair<Long, Long>?) {
        _filterSelectedDateRange.value = filterSelectedRange //Pair(startDate, endDate)
    }

    fun updateBillDialogState(updatedState: BillState) {
        _billDialogState.value = updatedState
    }

    fun updateIsAddOrEditSuccess(state: Boolean) {
        _isAddOrEditSuccess.value = state
    }

    fun resetBillDialogState() {
        _billDialogState.value = BillState()
        _isAddOrEditSuccess.value = false
    }

    fun applyDateRangeFilter(startDate: Long, endDate: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val billsByDate = getBillsByDateUseCase(startDate, endDate)
            _bills.value = billsByDate.sortedBy { bill ->
                bill.date
            }
        }
    }

    fun applyTextFilter(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val billsByText = getBillsByTextUseCase(text)
            _bills.value = billsByText
        }
    }

    fun clearFilter() {
        _filterSelectedDateRange.value = null
        getBills()
    }

    private fun getBills() {
        viewModelScope.launch(Dispatchers.IO) {
            val bills = getBillsUseCase()
            _bills.value = bills.reversed()
        }
    }

    fun saveBill(billState: BillState) {
        resetErrorMessages()

        viewModelScope.launch(Dispatchers.IO) {
            if(isBillStateValid()) {
                val bill = billState.toBill()
                saveBillUseCase(bill)

                updateIsAddOrEditSuccess(true)
                _billDialogState.value = BillState()

                getBills()
            }
        }
    }

    fun deleteTag(tag: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _billDialogState.value
            updateBillDialogState(
                currentState.copy(
                    tags = currentState.tags.filter {
                        it != tag
                    }
                )
            )

            updateBill(billDialogState.value, true)
        }
    }

    fun updateBill(billState: BillState, isUpdatingOnlyTags: Boolean = false) {
        resetErrorMessages()

        viewModelScope.launch(Dispatchers.IO) {
            if(isBillStateValid()) {
                val bill = billState.toBill()
                updateBillUseCase(bill)

                if(!isUpdatingOnlyTags) {
                    updateIsAddOrEditSuccess(true)
                    _billDialogState.value = BillState()
                    getBills()
                }
            }
        }
    }

    fun deleteBill(billState: BillState) {
        _isBillDeleted.value = false

        viewModelScope.launch(Dispatchers.IO)  {
            if(isBillStateValid()) {
                val bill = billState.toBill()
                deleteBillUseCase(bill)

                _isBillDeleted.value = true
                _billDialogState.value = BillState()

                getBills()
            }
        }
    }

    private fun resetErrorMessages() {
        updateBillDialogState(
            billDialogState.value.copy(
                titleErrorMessage = "",
                dateErrorMessage = "",
                valueErrorMessage = ""
            )
        )
    }

    private fun isBillStateValid() : Boolean {
        val currentState = _billDialogState.value

        val isTitleValid = validateTitle(billDialogState.value.title)
        val isDateValid = validateDate(billDialogState.value.date)
        val isValueValid = validateValue(billDialogState.value.value)

        updateBillDialogState(
            currentState.copy(
                titleErrorMessage = isTitleValid.second,
                dateErrorMessage = isDateValid.second,
                valueErrorMessage = isValueValid.second
            )
        )

        return isTitleValid.first && isDateValid.first && isValueValid.first
    }

    private fun validateTitle(title: String) : Pair<Boolean, String> {
        return if(title.isBlank()) {
            Pair(false, "O título não pode ser vazio")
        } else if(title.length < 3) {
            Pair(false, "O título é muito curto (min. 3 caracteres)")
        } else {
            Pair(true, "")
        }
    }

    private fun validateDate(date: Long) : Pair<Boolean, String> {
        return if(date == 0L) {
            Pair(false, "Você deve escolher uma data")
        } else if(date < 0) {
            Pair(false, "Data inválida")
        } else {
            Pair(true, "")
        }
    }

    private fun validateValue(value: Double) : Pair<Boolean, String> {
        return if(value == 0.0) {
            Pair(false, "Você deve informar o valor")
        } else if(value < 0) {
            Pair(false, "Valor inválido")
        } else {
            Pair(true, "")
        }
    }
}
