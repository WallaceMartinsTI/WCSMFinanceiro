package com.wcsm.wcsmfinanceiro.data.repository

import com.wcsm.wcsmfinanceiro.data.database.dao.WalletDao
import com.wcsm.wcsmfinanceiro.data.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.entity.relation.WalletWithCards
import com.wcsm.wcsmfinanceiro.domain.model.DatabaseResponse
import com.wcsm.wcsmfinanceiro.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val walletDao: WalletDao
) : WalletRepository {
    override suspend fun saveWallet(wallet: Wallet): Flow<DatabaseResponse<Long>> = flow {
        try {
            emit(DatabaseResponse.Loading)

            val response = walletDao.saveWallet(wallet)
            if(response > 0) {
                emit(DatabaseResponse.Success(response))
            } else {
                emit(DatabaseResponse.Error("Erro ao salvar carteira."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(DatabaseResponse.Error("Erro desconhecido ao salvar carteira, informe o administrador."))
        }
    }

    override suspend fun updateWallet(wallet: Wallet): Flow<DatabaseResponse<Int>> = flow {
        try {
            emit(DatabaseResponse.Loading)

            val response = walletDao.updateWallet(wallet)
            if(response > 0) {
                emit(DatabaseResponse.Success(response))
            } else {
                emit(DatabaseResponse.Error("Erro ao atualizar carteira."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(DatabaseResponse.Error("Erro desconhecido ao atualizar carteira, informe o administrador."))
        }
    }

    override suspend fun deleteWallet(wallet: Wallet): Flow<DatabaseResponse<Int>> = flow {
        try {
            emit(DatabaseResponse.Loading)

            val response = walletDao.deleteWallet(wallet)
            if(response > 0) {
                emit(DatabaseResponse.Success(response))
            } else {
                emit(DatabaseResponse.Error("Erro ao deletar carteira."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(DatabaseResponse.Error("Erro desconhecido ao deletar carteira, informe o administrador."))
        }
    }

    override suspend fun getWalletWithCards(): Flow<DatabaseResponse<List<WalletWithCards>>> = flow {
        try {
            emit(DatabaseResponse.Loading)

            emitAll(
                walletDao.selectAllWalletWithCards()
                    .map { walletWithCardsList ->
                        DatabaseResponse.Success(walletWithCardsList)
                    }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            emit(DatabaseResponse.Error("Erro desconhecido ao buscar carteiras, informe o administrador."))
        }
    }
}