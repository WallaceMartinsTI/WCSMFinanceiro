package com.wcsm.wcsmfinanceiro.domain.usecase.bills

import com.wcsm.wcsmfinanceiro.data.local.entity.Bill
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import kotlinx.coroutines.flow.Flow

class GetBillsByTextUseCase(
    private val billsRepository: BillsRepository
) {
    suspend operator fun invoke(text: String) : Flow<Response<List<Bill>>> {
        return billsRepository.getBillsByText(text)
    }
}