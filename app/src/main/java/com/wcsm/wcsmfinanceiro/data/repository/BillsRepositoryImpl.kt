package com.wcsm.wcsmfinanceiro.data.repository

import com.wcsm.wcsmfinanceiro.data.database.dao.BillsDao
import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.domain.model.DatabaseResponse
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BillsRepositoryImpl @Inject constructor(
    private val billsDao: BillsDao
) : BillsRepository {
    override suspend fun saveBill(bill: Bill): Flow<DatabaseResponse<Long>> = flow {
        try {
            emit(DatabaseResponse.Loading)

            val response = billsDao.saveBill(bill)
            if(response > 0) {
                emit(DatabaseResponse.Success(response))
            } else {
                emit(DatabaseResponse.Error("Erro ao salvar conta."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(DatabaseResponse.Error("Erro desconhecido ao salvar conta, informe o administrador."))
        }
    }

    override suspend fun updateBill(bill: Bill): Flow<DatabaseResponse<Int>> = flow {
        try {
            emit(DatabaseResponse.Loading)

            val response = billsDao.updateBill(bill)
            if(response > 0) {
                emit(DatabaseResponse.Success(response))
            } else {
                emit(DatabaseResponse.Error("Erro ao atualizar conta."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(DatabaseResponse.Error("Erro desconhecido ao atualizar conta, informe o administrador."))
        }
    }

    override suspend fun deleteBill(bill: Bill): Flow<DatabaseResponse<Int>> = flow {
        try {
            emit(DatabaseResponse.Loading)

            val response = billsDao.deleteBill(bill)
            if(response > 0) {
                emit(DatabaseResponse.Success(response))
            } else {
                emit(DatabaseResponse.Error("Erro ao deletar conta."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(DatabaseResponse.Error("Erro desconhecido ao deletar conta, informe o administrador."))
        }
    }

    override suspend fun getBills(): Flow<DatabaseResponse<List<Bill>>> = flow {
        try {
            emit(DatabaseResponse.Loading)

            emitAll(
                billsDao.selectAllBills()
                    .map { billsList ->
                        DatabaseResponse.Success(billsList)
                    }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            emit(DatabaseResponse.Error("Erro desconhecido ao buscar contas, informe o administrador."))
        }
    }

    override suspend fun getBillsByDate(
        startDate: Long,
        endDate: Long)
    : Flow<DatabaseResponse<List<Bill>>> = flow {
        try {
            emit(DatabaseResponse.Loading)

            emitAll(
                billsDao.selectBillsByDate(startDate, endDate)
                    .map { billsList ->
                        DatabaseResponse.Success(billsList)
                    }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            emit(DatabaseResponse.Error("Erro desconhecido ao buscar contas pela data, informe o administrador."))
        }
    }

    override suspend fun getBillsByText(text: String): Flow<DatabaseResponse<List<Bill>>> = flow {
        try {
            emit(DatabaseResponse.Loading)

            emitAll(
                billsDao.selectBillsByText(text)
                    .map { billsList ->
                        DatabaseResponse.Success(billsList)
                    }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            emit(DatabaseResponse.Error("Erro desconhecido ao buscar contas pelo texto, informe o administrador."))
        }
    }
}

