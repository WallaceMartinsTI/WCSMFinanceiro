package com.wcsm.wcsmfinanceiro.presentation.model

data class UiState<out T>(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val operationType: T? = null
)
