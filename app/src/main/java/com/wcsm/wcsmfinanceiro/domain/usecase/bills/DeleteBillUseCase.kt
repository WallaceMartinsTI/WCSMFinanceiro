package com.wcsm.wcsmfinanceiro.domain.usecase.bills

import com.wcsm.wcsmfinanceiro.data.local.entity.Bill
import com.wcsm.wcsmfinanceiro.data.local.model.BillType
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import com.wcsm.wcsmfinanceiro.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteBillUseCase @Inject constructor(
    private val billsRepository: BillsRepository,
    private val walletRepository: WalletRepository
) {
    suspend operator fun invoke(bill: Bill) : Flow<Response<Int>> {

        val walletWithCards = walletRepository.getWalletWithCardById(bill.walletId)

        val newValueAfterBillDeletion = if(bill.billType == BillType.INCOME) {
            walletWithCards.wallet.balance - bill.value
        } else {
            walletWithCards.wallet.balance + bill.value
        }
        walletRepository.updateWallet(
            wallet = walletWithCards.wallet.copy(balance = newValueAfterBillDeletion)
        ).collect {}

       return billsRepository.deleteBill(bill)
    }
}