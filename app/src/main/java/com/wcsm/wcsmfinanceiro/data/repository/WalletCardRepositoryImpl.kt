package com.wcsm.wcsmfinanceiro.data.repository

import com.wcsm.wcsmfinanceiro.data.database.dao.WalletCardDao
import com.wcsm.wcsmfinanceiro.data.entity.WalletCard
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.WalletCardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WalletCardRepositoryImpl @Inject constructor(
    private val walletCardDao: WalletCardDao
) : WalletCardRepository {
    override suspend fun saveWalletCard(
        walletCard: WalletCard
    ): Flow<Response<Long>> = flow {
        try {
            emit(Response.Loading)

            val response = walletCardDao.saveWalletCard(walletCard)
            if(response > 0) {
                emit(Response.Success(response))
            } else {
                emit(Response.Error("Erro ao salvar cartão."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error("Erro desconhecido ao salvar cartão, informe o administrador."))
        }
    }

    override suspend fun updateWalletCard(
        walletCard: WalletCard
    ): Flow<Response<Int>> = flow {
        try {
            emit(Response.Loading)

            val response = walletCardDao.updateWalletCard(walletCard)
            if(response > 0) {
                emit(Response.Success(response))
            } else {
                emit(Response.Error("Erro ao atualizar cartão."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error("Erro desconhecido ao atualizar cartão, informe o administrador."))
        }
    }

    override suspend fun deleteWalletCard(
        walletCard: WalletCard
    ): Flow<Response<Int>> = flow {
        try {
            emit(Response.Loading)

            val response = walletCardDao.deleteWalletCard(walletCard)
            if(response > 0) {
                emit(Response.Success(response))
            } else {
                emit(Response.Error("Erro ao deletar cartão."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error("Erro desconhecido ao deletar cartão, informe o administrador."))
        }
    }

}