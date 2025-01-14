package com.wcsm.wcsmfinanceiro.domain.entity

data class AccountCard(
    val id: Long,
    val title: String,
    val total: Double,
    val spent: Double,
    val remaining: Double
)
