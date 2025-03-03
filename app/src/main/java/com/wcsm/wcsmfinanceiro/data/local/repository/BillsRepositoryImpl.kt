package com.wcsm.wcsmfinanceiro.data.local.repository

import com.wcsm.wcsmfinanceiro.data.local.database.dao.BillsDao
import com.wcsm.wcsmfinanceiro.data.local.entity.Bill
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BillsRepositoryImpl @Inject constructor(
    private val billsDao: BillsDao
) : BillsRepository {
    override suspend fun saveBill(bill: Bill): Flow<Response<Long>> = flow {
        try {
            emit(Response.Loading)

            val response = billsDao.saveBill(bill)
            if(response > 0) {
                emit(Response.Success(response))
            } else {
                emit(Response.Error("Erro ao salvar conta."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error("Erro desconhecido ao salvar conta, informe o administrador."))
        }
    }

    override suspend fun updateBill(bill: Bill): Flow<Response<Int>> = flow {
        try {
            emit(Response.Loading)

            val response = billsDao.updateBill(bill)
            if(response > 0) {
                emit(Response.Success(response))
            } else {
                emit(Response.Error("Erro ao atualizar conta."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error("Erro desconhecido ao atualizar conta, informe o administrador."))
        }
    }

    override suspend fun deleteBill(bill: Bill): Flow<Response<Int>> = flow {
        try {
            emit(Response.Loading)

            val response = billsDao.deleteBill(bill)
            if(response > 0) {
                emit(Response.Success(response))
            } else {
                emit(Response.Error("Erro ao deletar conta."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error("Erro desconhecido ao deletar conta, informe o administrador."))
        }
    }

    override suspend fun getBills(): Flow<Response<List<Bill>>> = flow {
        try {
            emit(Response.Loading)

            emitAll(
                billsDao.selectAllBills()
                    .map { billsList ->
                        Response.Success(billsList)
                    }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error("Erro desconhecido ao buscar contas, informe o administrador."))
        }
    }

    override suspend fun getBillsByDate(
        startDate: Long,
        endDate: Long)
    : Flow<Response<List<Bill>>> = flow {
        try {
            emit(Response.Loading)

            emitAll(
                billsDao.selectBillsByDate(startDate, endDate)
                    .map { billsList ->
                        Response.Success(billsList)
                    }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error("Erro desconhecido ao buscar contas pela data, informe o administrador."))
        }
    }

    override suspend fun getBillsByText(text: String): Flow<Response<List<Bill>>> = flow {
        try {
            emit(Response.Loading)

            emitAll(
                billsDao.selectBillsByText(text)
                    .map { billsList ->
                        Response.Success(billsList)
                    }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error("Erro desconhecido ao buscar contas pelo texto, informe o administrador."))
        }
    }
}

