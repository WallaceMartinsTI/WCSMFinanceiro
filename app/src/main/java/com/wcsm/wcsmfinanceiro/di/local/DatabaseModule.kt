package com.wcsm.wcsmfinanceiro.di.local

import android.content.Context
import com.wcsm.wcsmfinanceiro.data.local.database.WCSMFinanceiroDatabase
import com.wcsm.wcsmfinanceiro.data.local.database.dao.BillsDao
import com.wcsm.wcsmfinanceiro.data.local.database.dao.WalletCardDao
import com.wcsm.wcsmfinanceiro.data.local.database.dao.WalletDao
import com.wcsm.wcsmfinanceiro.data.local.repository.BillsRepositoryImpl
import com.wcsm.wcsmfinanceiro.data.local.repository.WalletCardRepositoryImpl
import com.wcsm.wcsmfinanceiro.data.local.repository.WalletRepositoryImpl
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import com.wcsm.wcsmfinanceiro.domain.repository.WalletCardRepository
import com.wcsm.wcsmfinanceiro.domain.repository.WalletRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

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
    fun provideWalleDao(
        wcsmFinanceiroDatabase: WCSMFinanceiroDatabase
    ) : WalletDao {
        return wcsmFinanceiroDatabase.walletDao
    }

    @Provides
    fun provideWalletCardDao(
        wcsmFinanceiroDatabase: WCSMFinanceiroDatabase
    ) : WalletCardDao {
        return wcsmFinanceiroDatabase.walletCardDao
    }

    @Provides
    fun provideBillsRepository(
        billsDao: BillsDao
    ) : BillsRepository {
        return BillsRepositoryImpl(billsDao)
    }

    @Provides
    fun provideWalletRepository(
        walletDao: WalletDao
    ) : WalletRepository {
        return WalletRepositoryImpl(walletDao)
    }

    @Provides
    fun provideWalletCardRepository(
        walletCardDao: WalletCardDao
    ) : WalletCardRepository {
        return WalletCardRepositoryImpl(walletCardDao)
    }

}