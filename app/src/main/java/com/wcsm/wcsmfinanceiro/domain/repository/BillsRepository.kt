package com.wcsm.wcsmfinanceiro.domain.repository

import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.data.repository.RoomOperationResult

interface BillsRepository {
    suspend fun saveBill(bill2: Bill) : RoomOperationResult
    suspend fun updateBill(bill2: Bill) : RoomOperationResult
    suspend fun deleteBill(bill2: Bill) : RoomOperationResult
    suspend fun getBills() : List<Bill>
}