package com.wcsm.wcsmfinanceiro.di

import android.content.Context
import com.wcsm.wcsmfinanceiro.data.database.WCSMFinanceiroDatabase
import com.wcsm.wcsmfinanceiro.data.database.dao.BillsDao
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import com.wcsm.wcsmfinanceiro.data.repository.BillsRepositoryImpl
import com.wcsm.wcsmfinanceiro.domain.usecase.SaveBillUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideWCSMFinanceiroDatabas(
        @ApplicationContext context: Context
    ) : WCSMFinanceiroDatabase {
        return WCSMFinanceiroDatabase.getInstance(context)
    }

    @Provides
    fun provideBillsDao(
        wcsmFinanceiroDatabase: WCSMFinanceiroDatabase
    ) : BillsDao {
        return wcsmFinanceiroDatabase.billsDao
    }

    @Provides
    fun provideBillsRepository(billsDao: BillsDao) : BillsRepository {
        return BillsRepositoryImpl(billsDao)
    }

    @Provides
    fun provideSaveBillUseCase(
        billsRepository: BillsRepository
    ) : SaveBillUseCase {
        return SaveBillUseCase(billsRepository)
    }

}