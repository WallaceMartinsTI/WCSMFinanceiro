package com.wcsm.wcsmfinanceiro.domain.usecase.bills

import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import kotlinx.coroutines.flow.Flow

class GetBillsByDateUseCase(
    private val billsRepository: BillsRepository
) {
    suspend operator fun invoke(
        startDate: Long,
        endDate: Long
    ) : Flow<Response<List<Bill>>> {
        return billsRepository.getBillsByDate(startDate, endDate)
    }
}