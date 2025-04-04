package com.wcsm.wcsmfinanceiro.domain.repository

import com.wcsm.wcsmfinanceiro.data.local.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.local.entity.relation.WalletWithCards
import com.wcsm.wcsmfinanceiro.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    suspend fun saveWallet(wallet: Wallet) : Flow<Response<Long>>
    suspend fun updateWallet(wallet: Wallet) : Flow<Response<Int>>
    suspend fun deleteWallet(wallet: Wallet) : Flow<Response<Int>>
    suspend fun getWalletWithCards() : Flow<Response<List<WalletWithCards>>>
    suspend fun getWalletWithCardById(walletId: Long) : WalletWithCards
}