package com.wcsm.wcsmfinanceiro.domain.usecase.wallet

import com.wcsm.wcsmfinanceiro.data.entity.Wallet
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateWalletUseCase @Inject constructor(
    private val walletRepository: WalletRepository
) {
    suspend operator fun invoke(wallet: Wallet) : Flow<Response<Int>> {
        if(wallet.balance > 9999999.99) {
            return flow {
                emit(Response.Error("Valor muito alto (max. R$9.999.999,99)."))
            }
        }

        return walletRepository.updateWallet(wallet)
    }
}