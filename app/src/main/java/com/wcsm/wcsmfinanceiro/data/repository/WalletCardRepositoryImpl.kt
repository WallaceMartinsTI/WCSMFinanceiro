package com.wcsm.wcsmfinanceiro.data.repository

import com.wcsm.wcsmfinanceiro.data.database.dao.WalletCardDao
import com.wcsm.wcsmfinanceiro.data.entity.WalletCard
import com.wcsm.wcsmfinanceiro.domain.model.DatabaseResponse
import com.wcsm.wcsmfinanceiro.domain.repository.WalletCardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WalletCardRepositoryImpl @Inject constructor(
    private val walletCardDao: WalletCardDao
) : WalletCardRepository {
    override suspend fun saveWalletCard(
        walletCard: WalletCard
    ): Flow<DatabaseResponse<Long>> = flow {
        try {
            emit(DatabaseResponse.Loading)

            val response = walletCardDao.saveWalletCard(walletCard)
            if(response > 0) {
                emit(DatabaseResponse.Success(response))
            } else {
                emit(DatabaseResponse.Error("Erro ao salvar cartão."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(DatabaseResponse.Error("Erro desconhecido ao salvar cartão, informe o administrador."))
        }
    }

    override suspend fun updateWalletCard(
        walletCard: WalletCard
    ): Flow<DatabaseResponse<Int>> = flow {
        try {
            emit(DatabaseResponse.Loading)

            val response = walletCardDao.updateWalletCard(walletCard)
            if(response > 0) {
                emit(DatabaseResponse.Success(response))
            } else {
                emit(DatabaseResponse.Error("Erro ao atualizar cartão."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(DatabaseResponse.Error("Erro desconhecido ao atualizar cartão, informe o administrador."))
        }
    }

    override suspend fun deleteWalletCard(
        walletCard: WalletCard
    ): Flow<DatabaseResponse<Int>> = flow {
        try {
            emit(DatabaseResponse.Loading)

            val response = walletCardDao.deleteWalletCard(walletCard)
            if(response > 0) {
                emit(DatabaseResponse.Success(response))
            } else {
                emit(DatabaseResponse.Error("Erro ao deletar cartão."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(DatabaseResponse.Error("Erro desconhecido ao deletar cartão, informe o administrador."))
        }
    }

}