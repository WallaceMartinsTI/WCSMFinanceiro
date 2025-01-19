package com.wcsm.wcsmfinanceiro.domain.usecase

import android.util.Log
import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import javax.inject.Inject

class DeleteBillUseCase @Inject constructor(
    private val billsRepository: BillsRepository
) {
    suspend operator fun invoke(bill: Bill) {
       billsRepository.deleteBill(bill)
    }
}