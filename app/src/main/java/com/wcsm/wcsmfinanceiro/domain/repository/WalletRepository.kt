package com.wcsm.wcsmfinanceiro.domain.repository

import com.wcsm.wcsmfinanceiro.data.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.entity.relation.WalletWithCards
import com.wcsm.wcsmfinanceiro.domain.model.DatabaseResponse
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    suspend fun saveWallet(wallet: Wallet) : Flow<DatabaseResponse<Long>>
    suspend fun updateWallet(wallet: Wallet) : Flow<DatabaseResponse<Int>>
    suspend fun deleteWallet(wallet: Wallet) : Flow<DatabaseResponse<Int>>
    suspend fun getWalletWithCards() : Flow<DatabaseResponse<List<WalletWithCards>>>
}