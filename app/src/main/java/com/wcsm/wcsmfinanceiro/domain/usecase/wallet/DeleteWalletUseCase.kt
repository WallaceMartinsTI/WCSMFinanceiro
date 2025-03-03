package com.wcsm.wcsmfinanceiro.domain.usecase.wallet

import com.wcsm.wcsmfinanceiro.data.local.entity.Wallet
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteWalletUseCase @Inject constructor(
    private val walletRepository: WalletRepository
) {
    suspend operator fun invoke(wallet: Wallet) : Flow<Response<Int>> {
        return walletRepository.deleteWallet(wallet)
    }
}