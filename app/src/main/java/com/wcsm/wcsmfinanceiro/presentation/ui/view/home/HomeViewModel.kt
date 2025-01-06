package com.wcsm.wcsmfinanceiro.presentation.ui.view.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private val _filterSelectedDateRange = MutableStateFlow("")
    val filterSelectedDateRange: StateFlow<String> = _filterSelectedDateRange

    fun updateFilterSelectedDateRange(dateRange: String) {
        _filterSelectedDateRange.value = dateRange
    }

}