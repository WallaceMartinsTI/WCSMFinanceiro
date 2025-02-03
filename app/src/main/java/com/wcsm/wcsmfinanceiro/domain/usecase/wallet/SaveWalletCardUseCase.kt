package com.wcsm.wcsmfinanceiro.domain.usecase.wallet

import com.wcsm.wcsmfinanceiro.data.entity.WalletCard
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.WalletCardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveWalletCardUseCase @Inject constructor(
    private val walletCardRepository: WalletCardRepository
) {
    suspend operator fun invoke(walletCard: WalletCard) : Flow<Response<Long>> {
        if(walletCard.limit > 9999999.99) {
            return flow {
                emit(Response.Error("Valor limite muito alto (max. R$9.999.999,99)."))
            }
        }

        if(walletCard.spent > 9999999.99) {
            return flow {
                emit(Response.Error("Valor gasto muito alto (max. R$9.999.999,99)."))
            }
        } else if(walletCard.spent > walletCard.limit) {
            return flow {
                emit(Response.Error("Valor gasto não pode ser maior que o limite."))
            }
        }

        return walletCardRepository.saveWalletCard(walletCard)
    }
}