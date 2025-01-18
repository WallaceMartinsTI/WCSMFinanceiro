package com.wcsm.wcsmfinanceiro.presentation.ui.view.bills

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.domain.usecase.GetBillsUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.SaveBillUseCase
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
    private val saveBillUseCase: SaveBillUseCase
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

    //private val _filterSelectedDateRange = MutableStateFlow<Pair<Long, Long>?>(null)
    //val filterSelectedDateRange: StateFlow<Pair<Long, Long>?> = _filterSelectedDateRange

    private val _bills = MutableStateFlow<List<Bill>?>(null)
    val bills: StateFlow<List<Bill>?> = _bills

    /*fun updateFilterSelectedDateRange(startDate: Long, endDate: Long) {
        _filterSelectedDateRange.value = Pair(startDate, endDate)
    }*/

    init {
        getBills()
    }

    fun updateBillDialogState(updatedState: BillState) {
        _billDialogState.value = updatedState
    }

    fun updateBillDialogStateLoading(isLoading: Boolean) {
        val currentState = _billDialogState.value
        updateBillDialogState(
            currentState.copy(
                isLoading = isLoading
            )
        )
    }

    fun getBills() {
        viewModelScope.launch(Dispatchers.IO) {
            val bills = getBillsUseCase()
            _bills.value = bills
        }
    }

    fun saveBill(billState: BillState) {
        resetErrorMessages()

        updateBillDialogStateLoading(true)

        viewModelScope.launch(Dispatchers.IO) {
            if(isBillStateValid()) {
                val bill = billState.toBill()
                saveBillUseCase(bill)

                _billDialogState.value = BillState()
            }

            updateBillDialogStateLoading(false)
        }
    }

    fun resetErrorMessages() {
        Log.i("#-# TESTE #-#", "RESETOU AS MENSAGENS DE ERRO")
        val currentState = _billDialogState.value
        updateBillDialogState(
            currentState.copy(
                titleErrorMessage = "",
                dateErrorMessage = "",
                valueErrorMessage = ""
            )
        )
    }

    private fun isBillStateValid() : Boolean {
        val currentState = _billDialogState.value

        Log.i("#-# TESTE #-#", "VALIDOU")
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
