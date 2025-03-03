package com.wcsm.wcsmfinanceiro.domain.usecase.bills

import com.wcsm.wcsmfinanceiro.data.local.entity.Bill
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBillsUseCase @Inject constructor(
    private val billsRepository: BillsRepository
) {
    suspend operator fun invoke() : Flow<Response<List<Bill>>> {
        return billsRepository.getBills()
    }
}