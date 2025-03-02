package com.wcsm.wcsmfinanceiro.domain.usecase.wallet

import com.wcsm.wcsmfinanceiro.data.entity.WalletCard
import com.wcsm.wcsmfinanceiro.domain.model.DatabaseResponse
import com.wcsm.wcsmfinanceiro.domain.repository.WalletCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteWalletCardUseCase @Inject constructor(
    private val walletCardRepository: WalletCardRepository
) {
    suspend operator fun invoke(walletCard: WalletCard) : Flow<DatabaseResponse<Int>> {
        return walletCardRepository.deleteWalletCard(walletCard)
    }
}