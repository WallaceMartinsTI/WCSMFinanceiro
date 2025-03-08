package com.wcsm.wcsmfinanceiro.data.local.repository

import com.wcsm.wcsmfinanceiro.data.local.database.dao.SubscriptionDao
import com.wcsm.wcsmfinanceiro.data.local.entity.Subscription
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SubscriptionRepositoryImpl @Inject constructor(
    private val subscriptionDao: SubscriptionDao
) : SubscriptionRepository {
    override suspend fun saveSubscription(subscription: Subscription): Flow<Response<Long>> = flow {
        try {
            emit(Response.Loading)

            val response = subscriptionDao.saveSubscription(subscription)
            if(response > 0) {
                emit(Response.Success(response))
            } else {
                emit(Response.Error("Erro ao salvar assinatura."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error("Erro desconhecido ao salvar assinatura, informe o administrador."))
        }
    }

    override suspend fun updateSubscription(subscription: Subscription): Flow<Response<Int>> = flow {
        try {
            emit(Response.Loading)

            val response = subscriptionDao.updateSubscription(subscription)
            if(response > 0) {
                emit(Response.Success(response))
            } else {
                emit(Response.Error("Erro ao atualizar assinatura."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error("Erro desconhecido ao atualizar assinatura, informe o administrador."))
        }
    }

    override suspend fun deleteSubscription(subscription: Subscription): Flow<Response<Int>> = flow {
        try {
            emit(Response.Loading)

            val response = subscriptionDao.deleteSubscription(subscription)
            if(response > 0) {
                emit(Response.Success(response))
            } else {
                emit(Response.Error("Erro ao deletar assinatura."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error("Erro desconhecido ao deletar assinatura, informe o administrador."))
        }
    }

    override suspend fun getSubscriptions(): Flow<Response<List<Subscription>>> = flow {
        try {
            emit(Response.Loading)

            emitAll(
                subscriptionDao.selectAllSubscriptions()
                    .map { subscriptionsList ->
                        Response.Success(subscriptionsList)
                    }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error("Erro desconhecido ao buscar assinaturas, informe o administrador."))
        }
    }
}