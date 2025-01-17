package com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet

import androidx.lifecycle.ViewModel
import com.wcsm.wcsmfinanceiro.data.entity.Account
import com.wcsm.wcsmfinanceiro.data.entity.AccountCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WalletViewModel : ViewModel() {
    private val accountsList = listOf(
        Account(
            id = 1,
            title = "Nubank",
            balance = 1725.74,
            accountCards = listOf(
                AccountCard(
                    id = 1,
                    title = "Cartão de Crédito",
                    total = 5000.00,
                    spent = 1500.00,
                    remaining = 3500.00
                ),
                AccountCard(
                    id = 2,
                    title = "Cartão Adicional",
                    total = 5000.00,
                    spent = 1500.00,
                    remaining = 3500.00
                )
            )
        ),
        Account(
            id = 2,
            title = "Inter",
            balance = 1725.74,
            accountCards = listOf(
                AccountCard(
                    id = 3,
                    title = "Cartão de Crédito",
                    total = 5000.00,
                    spent = 1500.00,
                    remaining = 3500.00
                )
            )
        ),
        Account(
            id = 3,
            title = "Caixa",
            balance = 1725.74,
            accountCards = listOf(
                AccountCard(
                    id = 4,
                    title = "Cartão de Crédito",
                    total = 5000.00,
                    spent = 1500.00,
                    remaining = 3500.00
                ),
                AccountCard(
                    id = 5,
                    title = "Cartão Adicional",
                    total = 5000.00,
                    spent = 1500.00,
                    remaining = 3500.00
                ),
                AccountCard(
                    id = 6,
                    title = "Teste",
                    total = 5000.00,
                    spent = 1500.00,
                    remaining = 3500.00
                )
            )
        ),
        Account(
            id = 4,
            title = "Sicoob",
            balance = 1725.74,
            accountCards = listOf(
                AccountCard(
                    id = 4,
                    title = "Cartão de Crédito",
                    total = 5000.00,
                    spent = 1500.00,
                    remaining = 3500.00
                )
            )
        ),
        Account(
            id = 5,
            title = "Itaú",
            balance = 1725.74,
            accountCards = listOf(
                AccountCard(
                    id = 5,
                    title = "Cartão de Crédito",
                    total = 5000.00,
                    spent = 1500.00,
                    remaining = 3500.00
                )
            )
        ),
    )

    private val _wallets = MutableStateFlow<List<Account>?>(null)
    val wallets: StateFlow<List<Account>?> = _wallets

    init {
        _wallets.value = accountsList
    }
}