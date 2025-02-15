package com.wcsm.wcsmfinanceiro.data.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.data.database.WCSMFinanceiroDatabase
import com.wcsm.wcsmfinanceiro.data.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.entity.WalletCard
import com.wcsm.wcsmfinanceiro.data.entity.converter.BillConverter
import org.junit.After
import org.junit.Before
import org.junit.Test

class WalletCardDaoTest {
    private lateinit var wcsmFinanceiroDatabase: WCSMFinanceiroDatabase
    private lateinit var walletDao: WalletDao
    private lateinit var walletCardDao: WalletCardDao

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
        walletCardDao = wcsmFinanceiroDatabase.walletCardDao

        // Inserting a Wallet because a WalletCard needs to have a Wallet
        walletDao.saveWallet(Wallet(1, "Nubank", 125.32))
    }

    @After
    fun tearDown() {
        if(::wcsmFinanceiroDatabase.isInitialized) {
            wcsmFinanceiroDatabase.close()
        }
    }

    @Test
    fun saveWalletCard_saveWalletCardInRoomDB_returnWalletCardId() {
        // GIVEN: A wallet card is created
        val walletCard = WalletCard(
            walletId = 1,
            walletCardId = 1,
            title = "",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        // WHEN: The wallet card is saved in the Room database
        val walletCardId = walletCardDao.saveWalletCard(walletCard)

        // THEN: The returned wallet ID should be grater than 0, indicating a successful save
        assertThat(walletCardId).isGreaterThan(0L)
    }
}