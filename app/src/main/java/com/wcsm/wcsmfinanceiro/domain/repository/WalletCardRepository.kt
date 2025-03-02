package com.wcsm.wcsmfinanceiro.domain.repository

import com.wcsm.wcsmfinanceiro.data.entity.WalletCard
import com.wcsm.wcsmfinanceiro.domain.model.DatabaseResponse
import kotlinx.coroutines.flow.Flow

interface WalletCardRepository {
    suspend fun saveWalletCard(walletCard: WalletCard) : Flow<DatabaseResponse<Long>>
    suspend fun updateWalletCard(walletCard: WalletCard) : Flow<DatabaseResponse<Int>>
    suspend fun deleteWalletCard(walletCard: WalletCard) : Flow<DatabaseResponse<Int>>
}