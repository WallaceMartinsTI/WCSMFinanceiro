package com.wcsm.wcsmfinanceiro.domain.usecase.wallet

import com.wcsm.wcsmfinanceiro.data.entity.relation.WalletWithCards
import com.wcsm.wcsmfinanceiro.domain.model.DatabaseResponse
import com.wcsm.wcsmfinanceiro.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWalletWithCardsUseCase @Inject constructor(
    private val walletRepository: WalletRepository
) {
    suspend operator fun invoke() : Flow<DatabaseResponse<List<WalletWithCards>>> {
        return walletRepository.getWalletWithCards()
    }
}