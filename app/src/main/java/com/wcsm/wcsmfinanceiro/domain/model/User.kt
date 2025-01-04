package com.wcsm.wcsmfinanceiro.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String
)