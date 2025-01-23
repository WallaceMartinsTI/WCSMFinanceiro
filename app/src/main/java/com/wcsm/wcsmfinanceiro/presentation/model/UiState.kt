package com.wcsm.wcsmfinanceiro.presentation.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UiState(
    var isLoading: Boolean,
    var isSuccess: Boolean,
    var isError: Boolean
)

private val _uiState = MutableStateFlow(UiState(
    isLoading = false,
    isSuccess = false,
    isError = false))
val uiState = _uiState.asStateFlow()