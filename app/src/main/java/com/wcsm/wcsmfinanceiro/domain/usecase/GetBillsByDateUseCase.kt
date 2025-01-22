package com.wcsm.wcsmfinanceiro.domain.usecase

import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository

class GetBillsByDateUseCase(
    private val billsRepository: BillsRepository
) {
    suspend operator fun invoke(startDate: Long, endDate: Long) : List<Bill> {
        return billsRepository.getBillsBetweenDate(startDate, endDate)
    }
}