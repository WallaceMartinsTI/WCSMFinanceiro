package com.wcsm.wcsmfinanceiro.domain.repository

import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.data.repository.RoomOperationResult

interface BillsRepository {
    suspend fun saveBill(bill: Bill) : RoomOperationResult
    suspend fun updateBill(bill: Bill) : RoomOperationResult
    suspend fun deleteBill(bill: Bill) : RoomOperationResult
    suspend fun getBills() : List<Bill>
    suspend fun getBillsByDate(startDate: Long, endDate: Long) : List<Bill>
    suspend fun getBillsByText(text: String) : List<Bill>
}