package com.wcsm.wcsmfinanceiro.domain.usecase

import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository

class GetBillsByTextUseCase(
    private val billsRepository: BillsRepository
) {
    suspend operator fun invoke(text: String) : List<Bill> {
        return billsRepository.getBillsByText(text)
    }
}