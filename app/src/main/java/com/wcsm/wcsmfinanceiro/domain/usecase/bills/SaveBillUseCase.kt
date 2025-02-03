package com.wcsm.wcsmfinanceiro.domain.usecase.bills

import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveBillUseCase @Inject constructor(
    private val billsRepository: BillsRepository
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