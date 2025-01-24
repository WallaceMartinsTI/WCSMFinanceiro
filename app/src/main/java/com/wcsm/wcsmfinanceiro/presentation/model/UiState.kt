package com.wcsm.wcsmfinanceiro.presentation.model

data class UiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val operationType: OperationType? = null
)
