package com.wcsm.wcsmfinanceiro.data.repository

import com.wcsm.wcsmfinanceiro.data.database.dao.BillsDao
import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import javax.inject.Inject

class BillsRepositoryImpl @Inject constructor(
    private val billsDao: BillsDao
) : BillsRepository {
    override suspend fun saveBill(bill: Bill): RoomOperationResult {
        val billId = billsDao.saveBill(bill)
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

    override suspend fun updateBill(bill: Bill): RoomOperationResult {
        val billId = billsDao.updateBill(bill)
        if(billId > 0) {
            return RoomOperationResult(
                success = true,
                message = "Conta atualizada com sucesso."
            )
        }

        return RoomOperationResult(
            success = false,
            message = "Erro ao atualizar conta."
        )
    }

    override suspend fun deleteBill(bill: Bill): RoomOperationResult {
        val registerQuantity = billsDao.deleteBill(bill)
        if(registerQuantity > 0) {
            return RoomOperationResult(
                true, "Conta removida com sucesso."
            )
        }
        return RoomOperationResult(
            false, "Erro ao remover conta."
        )
    }

    override suspend fun getBills(): List<Bill> {
        return billsDao.selectAllBills()
    }

    override suspend fun getBillsByDate(startDate: Long, endDate: Long): List<Bill> {
        return billsDao.selectBillsByDate(startDate, endDate)
    }

    override suspend fun getBillsByText(text: String): List<Bill> {
        return billsDao.selectBillsByText(text)
    }
}

