package com.wcsm.wcsmfinanceiro.data.repository

import com.wcsm.wcsmfinanceiro.data.database.dao.WalletDao
import com.wcsm.wcsmfinanceiro.data.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.entity.relation.WalletWithCards
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val walletDao: WalletDao
) : WalletRepository {
    override suspend fun saveWallet(wallet: Wallet): Flow<Response<Long>> = flow {
        try {
            emit(Response.Loading)

            val response = walletDao.saveWallet(wallet)
            if(response > 0) {
                emit(Response.Success(response))
            } else {
                emit(Response.Error("Erro ao salvar carteira."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error("Erro desconhecido ao salvar carteira, informe o administrador."))
        }
    }

    override suspend fun updateWallet(wallet: Wallet): Flow<Response<Int>> = flow {
        try {
            emit(Response.Loading)

            val response = walletDao.updateWallet(wallet)
            if(response > 0) {
                emit(Response.Success(response))
            } else {
                emit(Response.Error("Erro ao atualizar carteira."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error("Erro desconhecido ao atualizar carteira, informe o administrador."))
        }
    }

    override suspend fun deleteWallet(wallet: Wallet): Flow<Response<Int>> = flow {
        try {
            emit(Response.Loading)

            val response = walletDao.deleteWallet(wallet)
            if(response > 0) {
                emit(Response.Success(response))
            } else {
                emit(Response.Error("Erro ao deletar carteira."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error("Erro desconhecido ao deletar carteira, informe o administrador."))
        }
    }

    override suspend fun getWalletWithCards(): Flow<Response<List<WalletWithCards>>> = flow {
        try {
            emit(Response.Loading)

            emitAll(
                walletDao.selectAllWalletWithCards()
                    .map { walletWithCardsList ->
                        Response.Success(walletWithCardsList)
                    }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error("Erro desconhecido ao buscar carteiras, informe o administrador."))
        }
    }
}