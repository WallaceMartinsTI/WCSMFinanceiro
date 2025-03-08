package com.wcsm.wcsmfinanceiro.domain.repository

import com.wcsm.wcsmfinanceiro.data.local.entity.Bill
import com.wcsm.wcsmfinanceiro.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface BillsRepository {
    suspend fun saveBill(bill: Bill): Flow<Response<Long>>
    suspend fun updateBill(bill: Bill): Flow<Response<Int>>
    suspend fun deleteBill(bill: Bill): Flow<Response<Int>>
    suspend fun getBills(): Flow<Response<List<Bill>>>
    suspend fun getBillsByDate(startDate: Long, endDate: Long): Flow<Response<List<Bill>>>
    suspend fun getBillsByText(text: String): Flow<Response<List<Bill>>>
}