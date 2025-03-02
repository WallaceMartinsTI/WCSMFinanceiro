package com.wcsm.wcsmfinanceiro.domain.usecase.bills

import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.domain.model.DatabaseResponse
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import kotlinx.coroutines.flow.Flow

class GetBillsByTextUseCase(
    private val billsRepository: BillsRepository
) {
    suspend operator fun invoke(text: String) : Flow<DatabaseResponse<List<Bill>>> {
        return billsRepository.getBillsByText(text)
    }
}