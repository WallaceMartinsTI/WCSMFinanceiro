package com.wcsm.wcsmfinanceiro.domain.repository

import com.wcsm.wcsmfinanceiro.data.entity.WalletCard
import com.wcsm.wcsmfinanceiro.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface WalletCardRepository {
    suspend fun saveWalletCard(walletCard: WalletCard) : Flow<Response<Long>>
    suspend fun updateWalletCard(walletCard: WalletCard) : Flow<Response<Int>>
    suspend fun deleteWalletCard(walletCard: WalletCard) : Flow<Response<Int>>
}