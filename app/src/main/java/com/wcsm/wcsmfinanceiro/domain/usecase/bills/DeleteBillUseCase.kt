package com.wcsm.wcsmfinanceiro.domain.usecase.bills

import com.wcsm.wcsmfinanceiro.data.local.entity.Bill
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteBillUseCase @Inject constructor(
    private val billsRepository: BillsRepository
) {
    suspend operator fun invoke(bill: Bill) : Flow<Response<Int>> {
       return billsRepository.deleteBill(bill)
    }
}