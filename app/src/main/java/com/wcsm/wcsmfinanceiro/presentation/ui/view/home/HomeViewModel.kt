package com.wcsm.wcsmfinanceiro.presentation.ui.view.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private val _filterSelectedDateRange = MutableStateFlow<Pair<Long, Long>?>(null)
    val filterSelectedDateRange: StateFlow<Pair<Long, Long>?> = _filterSelectedDateRange

    fun updateFilterSelectedDateRange(startDate: Long, endDate: Long) {
        _filterSelectedDateRange.value = Pair(startDate, endDate)
    }

}