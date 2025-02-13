package com.wcsm.wcsmfinanceiro.data.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.wcsm.wcsmfinanceiro.data.database.WCSMFinanceiroDatabase
import com.wcsm.wcsmfinanceiro.data.entity.converter.BillConverter
import org.junit.After
import org.junit.Before

class WalletDaoTest {
    private lateinit var wcsmFinanceiroDatabase: WCSMFinanceiroDatabase
    private lateinit var walletDao: WalletDao

    @Before
    fun setUp() {
        wcsmFinanceiroDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WCSMFinanceiroDatabase::class.java,
        )
            .addTypeConverter(BillConverter())
            .allowMainThreadQueries()
            .build()

        walletDao = wcsmFinanceiroDatabase.walletDao
    }

    @After
    fun tearDown() {
        if(::wcsmFinanceiroDatabase.isInitialized) {
            wcsmFinanceiroDatabase.close()
        }
    }
}