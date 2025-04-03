package com.wcsm.wcsmfinanceiro.domain.usecase.bills

import com.wcsm.wcsmfinanceiro.data.local.entity.Bill
import com.wcsm.wcsmfinanceiro.data.local.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.local.entity.relation.WalletWithCards
import com.wcsm.wcsmfinanceiro.data.local.model.BillType
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import com.wcsm.wcsmfinanceiro.domain.repository.WalletRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.job
import javax.inject.Inject

class SaveBillUseCase @Inject constructor(
    private val billsRepository: BillsRepository,
    private val walletRepository: WalletRepository
) {
    suspend operator fun invoke(bill: Bill) : Flow<Response<Long>> {
        if(bill.value > 9999999.99) {
            return flow {
                emit(Response.Error("Valor muito alto (max. R$9.999.999,99)."))
            }
        }

        return billsRepository.saveBill(bill)
    }
}