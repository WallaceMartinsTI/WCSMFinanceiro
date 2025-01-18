package com.wcsm.wcsmfinanceiro.data.repository

import com.wcsm.wcsmfinanceiro.data.database.dao.BillsDao
import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import javax.inject.Inject

class BillsRepositoryImpl @Inject constructor(
    private val billsDao: BillsDao
) : BillsRepository {
    override suspend fun saveBill(bill2: Bill): RoomOperationResult {
        val billId = billsDao.saveBill(bill2)
        if(billId > 0) {
            return RoomOperationResult(
                success = true,
                message = "Conta salva com sucesso."
            )
        }

        return RoomOperationResult(
            success = false,
            message = "Erro ao salvar conta."
        )
    }

    override suspend fun updateBill(bill2: Bill): RoomOperationResult {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBill(bill2: Bill): RoomOperationResult {
        TODO("Not yet implemented")
    }

    override suspend fun getBills(): List<Bill> {
        return billsDao.selectAllBills()
    }
}

