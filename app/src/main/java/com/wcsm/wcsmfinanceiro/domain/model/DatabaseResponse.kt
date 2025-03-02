package com.wcsm.wcsmfinanceiro.domain.model

sealed class DatabaseResponse<out T> {
    data object Loading : DatabaseResponse<Nothing>()

    data class Success<out T>(
        val data: T
    ) : DatabaseResponse<T>()

    data class Error(
        val message: String
    ) : DatabaseResponse<Nothing>()
}