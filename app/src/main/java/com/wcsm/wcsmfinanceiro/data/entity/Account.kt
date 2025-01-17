package com.wcsm.wcsmfinanceiro.data.entity

data class Account(
    val id: Long,
    val title: String,
    val balance: Double,
    val accountCards: List<AccountCard>
)
