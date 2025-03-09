package com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.viewmodel

import androidx.lifecycle.ViewModel
import com.wcsm.wcsmfinanceiro.presentation.model.plus.SubscriptionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SubscriptionViewModel : ViewModel() {
    private val _subscriptionStateFlow = MutableStateFlow(SubscriptionState())
    val subscriptionStateFlow = _subscriptionStateFlow.asStateFlow()

    fun updateSubscriptionState(updatedState: SubscriptionState) {
        _subscriptionStateFlow.value = updatedState
    }
}