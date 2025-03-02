package com.wcsm.wcsmfinanceiro.domain.repository

import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.domain.model.DatabaseResponse
import kotlinx.coroutines.flow.Flow

interface BillsRepository {
    suspend fun saveBill(bill: Bill) : Flow<DatabaseResponse<Long>>
    suspend fun updateBill(bill: Bill) : Flow<DatabaseResponse<Int>>
    suspend fun deleteBill(bill: Bill) : Flow<DatabaseResponse<Int>>
    suspend fun getBills() : Flow<DatabaseResponse<List<Bill>>>
    suspend fun getBillsByDate(startDate: Long, endDate: Long) : Flow<DatabaseResponse<List<Bill>>>
    suspend fun getBillsByText(text: String) : Flow<DatabaseResponse<List<Bill>>>
}